package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.controller.dto.CreditCardDTO;
import com.ironhack.midtermProject.model.AccountHolder;
import com.ironhack.midtermProject.model.CreditCard;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.CreditCardRepository;
import com.ironhack.midtermProject.service.interfaces.ICreditCardService;
import com.ironhack.midtermProject.utils.TimeCalculations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService implements ICreditCardService {
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    // Create new Credit Card account:
    public CreditCard create(CreditCardDTO creditCardDTO) {
        CreditCard creditCard = new CreditCard();
        creditCard.setBalance(new Money(creditCardDTO.getBalance()));

        // Check if specified primary owner in DTO exists in the repository:
        Optional<AccountHolder> primaryOwner = accountHolderRepository.findById(creditCardDTO.getPrimaryOwnerId());
        if (primaryOwner.isPresent()) {
            creditCard.setPrimaryOwner(primaryOwner.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with ID: " + creditCardDTO.getPrimaryOwnerId());
        }

        // If DTO has secondary owner...
        if (creditCardDTO.getSecondaryOwnerId() != null) {
            // Check if exists in the repository:
            Optional<AccountHolder> secondaryOwner = accountHolderRepository.findById(creditCardDTO.getSecondaryOwnerId());
            if (secondaryOwner.isPresent()) {
                creditCard.setSecondaryOwner(secondaryOwner.get());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with ID: " + creditCardDTO.getPrimaryOwnerId());
            }
        }

        // If DTO has a specific Credit Limit:
        if (creditCardDTO.getCreditLimit() != null) {             // If not, default is applied.
            creditCard.setCreditLimit(new Money(creditCardDTO.getCreditLimit()));
        }
        // If DTO has a specific Interest Rate:
        if (creditCardDTO.getInterestRate() != null) {             // If not, default is applied.
            creditCard.setInterestRate(creditCardDTO.getInterestRate());
        }

        return creditCardRepository.save(creditCard);
    }

    // Show all Credit Card accounts in repository:
    public List<CreditCard> showAll() {
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        // If list size is 0, there is no account to show.
        if (creditCardList.size() == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Credit Card Account to show.");
        } else {
            return creditCardList;
        }
    }

    // Find specific Credit Card account, by id:
    public Optional<CreditCard> find(Long id) {
        Optional<CreditCard> creditCard = creditCardRepository.findById(id);
        if (creditCard.isPresent()){
            return creditCard;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Credit Card Account with ID: " + id);
        }
    }

    // Method to update Interest:
    public void updateInterest(CreditCard creditCard){
        // Get months from last update, and check if it is greater than 1. If yes, apply it.
        int fromUpdated = TimeCalculations.calculateYears(creditCard.getInterestUpdate()) * 12
                        +   TimeCalculations.calculateMonths(creditCard.getInterestUpdate());
        if (fromUpdated >= 1) {
            for (int i = 1; i <= fromUpdated; i++){
                creditCard.getBalance().increaseAmount(
                        creditCard.getBalance().getAmount()
                                .multiply(creditCard.getInterestRate()).divide(new BigDecimal("12"), 2, RoundingMode.HALF_EVEN));
            }
            creditCard.setInterestUpdate(creditCard.getInterestUpdate().plusMonths(fromUpdated));

            creditCardRepository.save(creditCard);
        }
    }
}
