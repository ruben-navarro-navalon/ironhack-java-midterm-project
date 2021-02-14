package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.SavingsDTO;
import com.ironhack.midtermProject.model.Savings;

import java.util.List;
import java.util.Optional;

public interface ISavingsService {

    // The methods are described in the service implementation.

    public Savings create(SavingsDTO savingsDTO);
    public List<Savings> showAll();
    public Optional<Savings> find(Long id);
    public void updateInterest(Savings savings);
}
