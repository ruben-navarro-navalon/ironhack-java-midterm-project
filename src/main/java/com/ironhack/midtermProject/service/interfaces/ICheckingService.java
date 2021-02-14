package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.CheckingDTO;
import com.ironhack.midtermProject.model.Account;
import com.ironhack.midtermProject.model.Checking;

import java.util.List;
import java.util.Optional;

public interface ICheckingService {

    // The methods are described in the service implementation.

    public Account create(CheckingDTO checkingDTO);
    public List<Checking> showAll();
    public Optional<Checking> find(Long id);
    public void updateMaintenance(Checking checking);
}
