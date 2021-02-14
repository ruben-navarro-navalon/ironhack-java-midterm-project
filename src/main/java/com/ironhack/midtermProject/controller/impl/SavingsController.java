package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.SavingsDTO;
import com.ironhack.midtermProject.controller.interfaces.ISavingsController;
import com.ironhack.midtermProject.model.Savings;
import com.ironhack.midtermProject.service.interfaces.ISavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class SavingsController implements ISavingsController {
    @Autowired
    ISavingsService savingsService;

    // Create new Savings account. Only admins can do this:
    @PostMapping("/admin/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Savings create(@RequestBody @Valid SavingsDTO savingsDTO) {
        return savingsService.create(savingsDTO);
    }

    // Get all data from all Savings accounts. Only admins can do this:
    @GetMapping("/admin/savings")
    @ResponseStatus(HttpStatus.OK)
    public List<Savings> showAll() {
        return savingsService.showAll();
    }

    // Get all data from specific Savings account, by id. Only admins can do this:
    @GetMapping("/admin/savings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Savings> find(@PathVariable Long id) {
        return savingsService.find(id);
    }
}
