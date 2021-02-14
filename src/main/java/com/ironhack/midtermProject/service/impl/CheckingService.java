package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.controller.dto.CheckingDTO;
import com.ironhack.midtermProject.model.*;
import com.ironhack.midtermProject.repository.StudentCheckingRepository;
import com.ironhack.midtermProject.utils.TimeCalculations;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.CheckingRepository;
import com.ironhack.midtermProject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class CheckingService implements ICheckingService {
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    // Create new Checking account:
    public Account create(CheckingDTO checkingDTO) {
        // Check if specified primary owner in DTO exists in the repository:
        Optional<AccountHolder> primaryOwner = accountHolderRepository.findById(checkingDTO.getPrimaryOwnerId());
        if (primaryOwner.isPresent()) {
            // If primary owner age is over 24, new Checking account is created:
            if (TimeCalculations.calculateYears(primaryOwner.get().getBirthDate()) > 24){
                Checking checking = new Checking();
                checking.setBalance(new Money(checkingDTO.getBalance()));
                checking.setPrimaryOwner(primaryOwner.get());

                // If DTO has secondary owner...
                if (checkingDTO.getSecondaryOwnerId() != null) {
                    // Check if exists in the repository:
                    Optional<AccountHolder> secondaryOwner = accountHolderRepository.findById(checkingDTO.getSecondaryOwnerId());
                    if (secondaryOwner.isPresent()) {
                        checking.setSecondaryOwner(secondaryOwner.get());
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with ID: " + checkingDTO.getPrimaryOwnerId());
                    }
                }
                checking.setSecretKey(checkingDTO.getSecretKey());
                return checkingRepository.save(checking);
            } else {
                // If primary owner age is under 24, new Student Checking account is created:
                StudentChecking studentChecking = new StudentChecking();
                studentChecking.setBalance(new Money(checkingDTO.getBalance()));
                studentChecking.setPrimaryOwner(primaryOwner.get());

                // If DTO has secondary owner...
                if (checkingDTO.getSecondaryOwnerId() != null) {
                    // Check if exists in the repository:
                    Optional<AccountHolder> secondaryOwner = accountHolderRepository.findById(checkingDTO.getSecondaryOwnerId());
                    if (secondaryOwner.isPresent()) {
                        studentChecking.setSecondaryOwner(secondaryOwner.get());
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with ID: " + checkingDTO.getPrimaryOwnerId());
                    }
                }
                studentChecking.setSecretKey(checkingDTO.getSecretKey());
                return studentCheckingRepository.save(studentChecking);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with ID: " + checkingDTO.getPrimaryOwnerId());
        }
    }

    // Show all Checking accounts in repository:
    public List<Checking> showAll() {
        List<Checking> checkingList = checkingRepository.findAll();
        // If list size is 0, there is no account to show.
        if (checkingList.size() == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Checking Account to show.");
        } else {
            return checkingList;
        }
    }

    // Find specific Checking account, by id:
    public Optional<Checking> find(Long id) {
        Optional<Checking> checking = checkingRepository.findById(id);
        if (checking.isPresent()){
            return checking;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Checking Account with ID: " + id);
        }
    }

    // Method to update Maintenance Fee:
    public void updateMaintenance(Checking checking) {
        // Get months from last update, and check if it is greater than 1. If yes, apply it.
        int fromUpdated = TimeCalculations.calculateYears(checking.getMaintenanceUpdate()) * 12
                        +   TimeCalculations.calculateMonths(checking.getMaintenanceUpdate());
        if (fromUpdated >= 1) {
            for (int i = 1; i <= fromUpdated; i++) {
                checking.getBalance().decreaseAmount(checking.getMonthlyMaintenanceFee());
            }
            checking.setMaintenanceUpdate(checking.getMaintenanceUpdate().plusMonths(fromUpdated));
            checkingRepository.save(checking);
        }
    }
}