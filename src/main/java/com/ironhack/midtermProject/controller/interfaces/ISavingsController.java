package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.SavingsDTO;
import com.ironhack.midtermProject.model.Savings;

import java.util.List;
import java.util.Optional;

public interface ISavingsController {

    // The methods are described in the controller implementation.

    public Savings create(SavingsDTO savingsDTO);
    public List<Savings> showAll();
    public Optional<Savings> find(Long id);
}
