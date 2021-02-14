package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.CheckingDTO;
import com.ironhack.midtermProject.model.Account;
import com.ironhack.midtermProject.model.Checking;

import java.util.List;
import java.util.Optional;

public interface ICheckingController {

    // The methods are described in the controller implementation.

    public Account create(CheckingDTO checkingDTO);
    public List<Checking> showAll();
    public Optional<Checking> find(Long id);
}
