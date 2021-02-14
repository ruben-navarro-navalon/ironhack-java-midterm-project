package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.controller.dto.SavingsDTO;
import com.ironhack.midtermProject.model.AccountHolder;
import com.ironhack.midtermProject.model.Savings;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.SavingsRepository;
import com.ironhack.midtermProject.service.interfaces.ISavingsService;
import com.ironhack.midtermProject.utils.TimeCalculations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SavingsService implements ISavingsService {
    @Autowired
    SavingsRepository savingsRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    // Create new Savings account:
    public Savings create(SavingsDTO savingsDTO) {
        Savings savings = new Savings();
        savings.setBalance(new Money(savingsDTO.getBalance()));

        // Check if specified primary owner in DTO exists in the repository:
        Optional<AccountHolder> primaryOwner = accountHolderRepository.findById(savingsDTO.getPrimaryOwnerId());
        if (primaryOwner.isPresent()) {
            savings.setPrimaryOwner(primaryOwner.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with ID: " + savingsDTO.getPrimaryOwnerId());
        }

        // If DTO has secondary owner...
        if (savingsDTO.getSecondaryOwnerId() != null) {
            // Check if exists in the repository:
            Optional<AccountHolder> secondaryOwner = accountHolderRepository.findById(savingsDTO.getSecondaryOwnerId());
            if (secondaryOwner.isPresent()) {
                savings.setSecondaryOwner(secondaryOwner.get());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with ID: " + savingsDTO.getPrimaryOwnerId());
            }
        }

        // If DTO has a specific Minimum Balance:
        if (savingsDTO.getMinimumBalance() != null) {           // If not, default is applied.
            savings.setMinimumBalance(new Money(savingsDTO.getMinimumBalance()));
        }
        // If DTO has a specific Interest Rate:
        if (savingsDTO.getInterestRate() != null) {             // If not, default is applied.
            savings.setInterestRate(savingsDTO.getInterestRate());
        }
        savings.setSecretKey(savingsDTO.getSecretKey());

        return savingsRepository.save(savings);
    }

    // Show all Savings accounts in repository:
    public List<Savings> showAll() {
        List<Savings> savingsList = savingsRepository.findAll();
        // If list size is 0, there is no account to show.
        if (savingsList.size() == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Savings Account to show.");
        } else {
            return savingsList;
        }
    }

    // Find specific Savings account, by id:
    public Optional<Savings> find(Long id) {
        Optional<Savings> savings = savingsRepository.findById(id);
        if (savings.isPresent()){
            return savings;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Savings Account with ID: " + id);
        }
    }

    // Method to update Interest:
    public void updateInterest(Savings savings){
        // Get years from last update, and check if it is greater than 1. If yes, apply it.
        int fromUpdated = TimeCalculations.calculateYears(savings.getInterestUpdate());
        if (fromUpdated >= 1) {
            for (int i = 1; i <= fromUpdated; i++) {
                savings.getBalance().increaseAmount(savings.getBalance().getAmount().multiply(savings.getInterestRate()));
            }
            savings.setInterestUpdate(savings.getInterestUpdate().plusYears(fromUpdated));
            savingsRepository.save(savings);
        }
    }

}
