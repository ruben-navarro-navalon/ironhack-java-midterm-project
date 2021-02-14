package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.CheckingDTO;
import com.ironhack.midtermProject.controller.interfaces.ICheckingController;
import com.ironhack.midtermProject.model.Account;
import com.ironhack.midtermProject.model.Checking;
import com.ironhack.midtermProject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class CheckingController implements ICheckingController {
    @Autowired
    ICheckingService checkingService;

    // Create new Checking account. Only admins can do this. If owner is under 24, a Student Checking is created instead:
    @PostMapping("/admin/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestBody @Valid CheckingDTO checkingDTO) {
        return checkingService.create(checkingDTO);
    }

    // Get all data from all Checking accounts. Only admins can do this:
    @GetMapping("/admin/checking")
    @ResponseStatus(HttpStatus.OK)
    public List<Checking> showAll() {
        return checkingService.showAll();
    }

    // Get all data from specific Checking account, by id. Only admins can do this:
    @GetMapping("/admin/checking/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Checking> find(@PathVariable Long id) {
        return checkingService.find(id);
    }

}
