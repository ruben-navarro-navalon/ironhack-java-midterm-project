package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.controller.dto.MoneyDTO;
import com.ironhack.midtermProject.controller.dto.TransferDTO;
import com.ironhack.midtermProject.enums.Status;
import com.ironhack.midtermProject.model.*;
import com.ironhack.midtermProject.repository.*;
import com.ironhack.midtermProject.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SavingsService savingsService;
    @Autowired
    CreditCardService creditCardService;
    @Autowired
    CheckingService checkingService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    TransferRepository transferRepository;



    // Check account balance, by id. Only admins or account owners can do this:
    public Money checkBalance(Long id, Principal principal) {
        // If no user is logged in:
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must login");
        }
        String loggedUser = principal.getName();
        // If logged, check if account exists in repository:
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            // If account exists, check if logged user is admin or owner.
            if(!checkLoggedIsOwnerOrAdmin(account.get(), loggedUser)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User " + loggedUser + " is not the owner of the account.");
            }
            // If account type is Savings or Credit Card, check if interest must be updated:
            if (account.get() instanceof Savings) {
                savingsService.updateInterest((Savings) account.get());
            } else if (account.get() instanceof CreditCard){
                creditCardService.updateInterest((CreditCard) account.get());
            } else if (account.get() instanceof Checking){
                checkingService.updateMaintenance((Checking) account.get());
            }
            return account.get().getBalance();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no account with ID: " + id);
        }
    }

    // Create new transfer:
    public Transfer transfer(TransferDTO transferDTO, Principal principal) {
        // If no user is logged in:
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must login");
        }
        String loggedUser = principal.getName();

        // If logged, check if accounts exists in repository:
        Optional<Account> originAccount = accountRepository.findById(transferDTO.getOriginAccountId());
        Optional<Account> destinationAccount = accountRepository.findById(transferDTO.getDestinationAccountId());
        if (originAccount.isPresent() && destinationAccount.isPresent()){
            // Check if logged user is Owner of the origin Account:
            if(!checkLoggedIsOwner(originAccount.get(), loggedUser)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User " + loggedUser + " is not the owner of the account.");
            }

            // Check if destination name is Owner of the destination Account:
            if(!checkDestinationNameIsDestinationOwner(transferDTO, destinationAccount.get())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Destination Name '" + transferDTO.getDestinationName() + "' is not the owner of the Destination account.");
            }

            // Check if origin Account has sufficient funds:
            if(!checkOriginAccountHasFunds(transferDTO, originAccount.get())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Origin Account has not enough funds.");
            }

            // Check frozen account:
            if (checkFreeze(originAccount.get()) || checkFreeze(destinationAccount.get())){
                throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "Account is frozen. Let it go...");
            }
            // Check fraud on account:
            if (checkFraud(originAccount.get(), transferDTO.getTransferAmount())) {
                throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "Fraud detected. Batman is coming to you!");
            }


            // Transfer done:
            originAccount.get().getBalance().decreaseAmount(transferDTO.getTransferAmount());
            destinationAccount.get().getBalance().increaseAmount(transferDTO.getTransferAmount());

            // Check penalty fee in Origin Account, and apply it if needed:
            if(checkPenaltyFee(originAccount.get())){
                originAccount.get().getBalance().decreaseAmount(originAccount.get().getPenaltyFee());
                if (originAccount.get() instanceof Savings) {
                    ((Savings) originAccount.get()).setBelowMinimumBalance(true);
                }
                if (originAccount.get() instanceof Checking) {
                    ((Checking) originAccount.get()).setBelowMinimumBalance(true);
                }
            }

            // Check if Destination Account has raised above the minimum Balance:
            if (destinationAccount.get() instanceof Savings){
                if(destinationAccount.get().getBalance().getAmount()
                        .compareTo(((Savings) destinationAccount.get()).getMinimumBalance().getAmount()) > 0
                && ((Savings) destinationAccount.get()).getBelowMinimumBalance()) {
                    ((Savings) destinationAccount.get()).setBelowMinimumBalance(false);
                }
            } else if (destinationAccount.get() instanceof Checking) {
                if (destinationAccount.get().getBalance().getAmount()
                        .compareTo(((Checking) destinationAccount.get()).getMinimumBalance().getAmount()) > 0
                && ((Checking) destinationAccount.get()).getBelowMinimumBalance()) {
                    ((Checking) destinationAccount.get()).setBelowMinimumBalance(false);
                }
            }

            // Save objects in repositories:
            accountRepository.save(originAccount.get());
            accountRepository.save(destinationAccount.get());

            Transfer transfer = new Transfer(
                    originAccount.get(),
                    destinationAccount.get(),
                    transferDTO.getDestinationName(),
                    transferDTO.getConcept(),
                    new Money(transferDTO.getTransferAmount()));

            return transferRepository.save(transfer);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin or destination account doesn't exist.");
        }
    }

    // Check if an account is Frozen or is making fraud. Remember: Batman is watching.
    private boolean checkFraud(Account account, BigDecimal amount) {
        if(transferRepository.lastSecondFraud(account.getId()).size() >= 2) {
            freezeAccount(account);
            return true;
        }
        if (transferRepository.maxIn24hours(account.getId()) != null) {
            if (transferRepository.last24hours(account.getId()) == null) {
                if (amount.compareTo(transferRepository.maxIn24hours(account.getId()).multiply(new BigDecimal("1.5")))>0) {
                    freezeAccount(account);
                    return true;
                }
            }
            if (transferRepository.last24hours(account.getId()).add(amount).compareTo(transferRepository.maxIn24hours(account.getId()).multiply(new BigDecimal("1.5")))>0) {
                freezeAccount(account);
                return true;
            }
        }
        return false;
    }

    // Check if Mr.Freeze has come to you:
    public boolean checkFreeze(Account account) {
        if (account instanceof Checking){
            return ((Checking) account).getStatus().equals(Status.FROZEN);
        } else if (account instanceof StudentChecking){
            return ((StudentChecking) account).getStatus().equals(Status.FROZEN);
        } else if (account instanceof Savings){
            return ((Savings) account).getStatus().equals(Status.FROZEN);
        }
        return false;
    }

    // Mr.Freeze is coming to town:
    public void freezeAccount(Account account) {
        if (account instanceof Checking){
            ((Checking) account).setStatus(Status.FROZEN);
        } else if (account instanceof StudentChecking){
            ((StudentChecking) account).setStatus(Status.FROZEN);
        } else if (account instanceof Savings){
            ((Savings) account).setStatus(Status.FROZEN);
        }
        accountRepository.save(account);
    }


    // Create new transfer from Account Holder to ThirdParty:
    public Transfer transferToThirdParty(TransferDTO transferDTO, String hashKey, String secretKey) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Optional<Account> originAccount = accountRepository.findById(transferDTO.getOriginAccountId());
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findById(transferDTO.getDestinationAccountId());

        // Check if origin account from DTO exists in repository:
        if (!originAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Account with ID: " + transferDTO.getOriginAccountId());
        }
        // Check if provided secret key matches with origin account:
        if(!checkSecretKey(originAccount.get(), secretKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Secret Key for account with ID: " + originAccount.get().getId());
        }
        // Check if third party from DTO exists in repository:
        if (!thirdParty.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Third Party with ID: " + transferDTO.getDestinationAccountId());
        }
        // Check if Destination Name in DTO matches third party:
        if (!transferDTO.getDestinationName().equals(thirdParty.get().getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong name for Third Party with ID: "+ thirdParty.get().getId());
        }
        // Check if provided hash key matches with third party:
        if (!passwordEncoder.matches(hashKey, thirdParty.get().getHashKey())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong HashKey for Third party with ID: " + thirdParty.get().getId());
        }
        // Check if origin Account has sufficient funds:
        if(!checkOriginAccountHasFunds(transferDTO, originAccount.get())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Origin Account has not enough funds.");
        }
        // Check frozen account:
        if (checkFreeze(originAccount.get())){
            throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "Account is frozen. Let it go...");
        }
        // Check fraud on account:
        if (checkFraud(originAccount.get(), transferDTO.getTransferAmount())) {
            throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "Fraud detected. Batman is coming to you!");
        }

        // Transfer done:
        originAccount.get().getBalance().decreaseAmount(transferDTO.getTransferAmount());

        // Check penalty fee in Origin Account, and apply it if needed:
        if(checkPenaltyFee(originAccount.get())){
            originAccount.get().getBalance().decreaseAmount(originAccount.get().getPenaltyFee());
            if (originAccount.get() instanceof Savings) {
                ((Savings) originAccount.get()).setBelowMinimumBalance(true);
            }
            if (originAccount.get() instanceof Checking) {
                ((Checking) originAccount.get()).setBelowMinimumBalance(true);
            }
        }

        // Save objects in repositories:
        accountRepository.save(originAccount.get());

        Transfer transfer = new Transfer(
                originAccount.get(),
                null,
                transferDTO.getDestinationName(),
                transferDTO.getConcept(),
                new Money(transferDTO.getTransferAmount()));

        return transferRepository.save(transfer);
    }


    // Create new transfer from ThirdParty to Account Holder:
    public Transfer transferFromThirdParty(TransferDTO transferDTO, String hashKey, String secretKey) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findById(transferDTO.getOriginAccountId());
        Optional<Account> destinationAccount = accountRepository.findById(transferDTO.getDestinationAccountId());

        // Check if third party from DTO exists in repository:
        if (!thirdParty.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Third Party with ID: " + transferDTO.getOriginAccountId());
        }
        // Check if provided hash key matches with third party:
        if (!passwordEncoder.matches(hashKey, thirdParty.get().getHashKey())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong HashKey for Third party with ID: " + thirdParty.get().getId());
        }
        // Check if destination account from DTO exists in repository:
        if (!destinationAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Account with ID: " + transferDTO.getDestinationAccountId());
        }
        // Check if Destination Name in DTO matches destination account:
        if (!checkDestinationNameIsDestinationOwner(transferDTO, destinationAccount.get())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Destination Name '" + transferDTO.getDestinationName() + "' is not the owner of the Destination account.");
        }
        // Check if provided secret key matches with destination account:
        if(!checkSecretKey(destinationAccount.get(), secretKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Secret Key for account with ID: " + destinationAccount.get().getId());
        }
        // Check frozen account:
        if (checkFreeze(destinationAccount.get())){
            throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "Account is frozen. Let it go...");
        }
        // Check fraud account:
        if (checkFraud(destinationAccount.get(), transferDTO.getTransferAmount())) {
            throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "Fraud detected. Batman is coming to you!");
        }

        // Transfer done:
        destinationAccount.get().getBalance().increaseAmount(transferDTO.getTransferAmount());

        // Check if Destination Account has raised above the minimum Balance:
        if (destinationAccount.get() instanceof Savings){
            if(destinationAccount.get().getBalance().getAmount()
                    .compareTo(((Savings) destinationAccount.get()).getMinimumBalance().getAmount()) > 0
                    && ((Savings) destinationAccount.get()).getBelowMinimumBalance()) {
                ((Savings) destinationAccount.get()).setBelowMinimumBalance(false);
            }
        } else if (destinationAccount.get() instanceof Checking) {
            if (destinationAccount.get().getBalance().getAmount()
                    .compareTo(((Checking) destinationAccount.get()).getMinimumBalance().getAmount()) > 0
                    && ((Checking) destinationAccount.get()).getBelowMinimumBalance()) {
                ((Checking) destinationAccount.get()).setBelowMinimumBalance(false);
            }
        }

        // Save objects in repository:
        accountRepository.save(destinationAccount.get());

        Transfer transfer = new Transfer(
                null,
                destinationAccount.get(),
                transferDTO.getDestinationName(),
                transferDTO.getConcept(),
                new Money(transferDTO.getTransferAmount()));

        return transferRepository.save(transfer);
    }


    // Modify account balance, by id. Only admins can do this:
    public void modifyBalance(Long id, MoneyDTO newBalance) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()){
            account.get().setBalance(new Money(newBalance.getAmount()));

            // If Account type is Savings or Checking, check if new Balance is under or over Minimum Balance, and set:
            if (account.get() instanceof Savings) {
                if (account.get().getBalance().getAmount().compareTo(((Savings) account.get()).getMinimumBalance().getAmount()) >= 0) {
                    ((Savings) account.get()).setBelowMinimumBalance(false);
                } else {
                    ((Savings) account.get()).setBelowMinimumBalance(true);
                }
            } else if (account.get() instanceof Checking) {
                if (account.get().getBalance().getAmount().compareTo(((Checking) account.get()).getMinimumBalance().getAmount()) >= 0) {
                    ((Checking) account.get()).setBelowMinimumBalance(false);
                } else {
                    ((Checking) account.get()).setBelowMinimumBalance(true);
                }
            }

            accountRepository.save(account.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Account with ID: " + id);
        }

    }

    // Check if a logged user is admin or owner:
    public boolean checkLoggedIsOwnerOrAdmin(Account account, String loggedUser) {
        // Check if logged user exists in repository:
        Optional<User> user = userRepository.findByUsername(loggedUser);
        if (user.isPresent()){
            // If it is also present in Admin repo, logged user is an Admin:
            if (adminRepository.findById(user.get().getId()).isPresent()) {
                return true;

            } // Or, if it is also present in Account Holder repo, logged user is an Account Holder:
            else if (accountHolderRepository.findById(user.get().getId()).isPresent()) {

                // Check if logged user is the primary owner of the account:
                if (account.getPrimaryOwner().getUsername().equals(user.get().getUsername())) {
                    return true;
                } // Or, if account has a secondary owner, check if it is equals to logged user:
                else if (account.getSecondaryOwner() != null) {
                    return account.getSecondaryOwner().getUsername().equals(user.get().getUsername());
                }
            }
            return false;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + loggedUser + " doesn't exist.");
        }
    }

    // Check if a logged user is owner:
    public boolean checkLoggedIsOwner(Account account, String loggedUser) {
        // Check if logged user exists in repository:
        Optional<User> user = userRepository.findByUsername(loggedUser);
        if (user.isPresent()){
            // Tf it is also present in Account Holder repo, logged user is an Account Holder:
            if (accountHolderRepository.findById(user.get().getId()).isPresent()) {

                // Check if logged user is the primary owner of the account:
                if (account.getPrimaryOwner().getUsername().equals(user.get().getUsername())) {
                    return true;
                } // Or, if account has a secondary owner, check if it is equals to logged user:
                else if (account.getSecondaryOwner() != null) {
                    return account.getSecondaryOwner().getUsername().equals(user.get().getUsername());
                }
            }
            return false;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + loggedUser + " doesn't exist.");
        }
    }

    // Check if Destination Name of a transfer is owner of the Destination Account of the transfer:
    public boolean checkDestinationNameIsDestinationOwner(TransferDTO transferDTO, Account destinationAccount) {
        // Check primary owner:
        if (transferDTO.getDestinationName().equals(destinationAccount.getPrimaryOwner().getName())) {
            return true;
        } // Or check secondary owner if exists:
        else if (destinationAccount.getSecondaryOwner() != null) {
            return transferDTO.getDestinationName().equals(destinationAccount.getSecondaryOwner().getName());
        }
        return false;
    }

    // Check if Origin Account of a transfer has enough funds to make it:
    public boolean checkOriginAccountHasFunds(TransferDTO transferDTO, Account originAccount){
        BigDecimal funds = originAccount.getBalance().getAmount();
        BigDecimal transferAmount = transferDTO.getTransferAmount();

        if (originAccount instanceof CreditCard){   // If Credit Card, balance is allowed to be negative above "Negative Credit Limit"
            return funds
                    .subtract(transferAmount)
                    .compareTo(
                            ((CreditCard) originAccount).getCreditLimit().getAmount().negate()) >= 0;
        } else {    // Other types of accounts should be always 0 or more.
            return funds
                    .subtract(transferAmount)
                    .compareTo(BigDecimal.ZERO) >= 0;
        }
    }

    // Check if Penalty Fee has to be applied after a transfer (only Savings and Checking accounts):
    public boolean checkPenaltyFee(Account account) {
        if (account instanceof Savings) {
            return account.getBalance().getAmount()
                        .compareTo(
                                ((Savings) account).getMinimumBalance().getAmount()) < 0
                    &&
                    !((Savings) account).getBelowMinimumBalance();
        } else if (account instanceof Checking) {
            return account.getBalance().getAmount()
                        .compareTo(
                                ((Checking) account).getMinimumBalance().getAmount()) < 0
                    &&
                    !((Checking) account).getBelowMinimumBalance();
        }
        return false;
    }

    // Check secret key:
    public boolean checkSecretKey(Account account, String secretKey){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (account instanceof CreditCard) {
            return true;
        } else if (account instanceof Savings){
            return passwordEncoder.matches(secretKey, ((Savings) account).getSecretKey());
        } else if (account instanceof Checking) {
            return passwordEncoder.matches(secretKey, ((Checking) account).getSecretKey());
        }else if (account instanceof StudentChecking) {
            return passwordEncoder.matches(secretKey, ((StudentChecking) account).getSecretKey());
        }
        return false;
    }


}
