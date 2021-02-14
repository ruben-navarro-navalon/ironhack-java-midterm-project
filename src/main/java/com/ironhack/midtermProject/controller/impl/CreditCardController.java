package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.CreditCardDTO;
import com.ironhack.midtermProject.controller.interfaces.ICreditCardController;
import com.ironhack.midtermProject.model.CreditCard;
import com.ironhack.midtermProject.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class CreditCardController implements ICreditCardController {
    @Autowired
    ICreditCardService creditCardService;

    // Create new Credit Card account. Only admins can do this:
    @PostMapping("/admin/credit-card")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard create(@RequestBody @Valid CreditCardDTO creditCardDTO) {
        return creditCardService.create(creditCardDTO);
    }

    // Get all data from all Credit Card accounts. Only admins can do this:
    @GetMapping("/admin/credit-card")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> showAll() {
        return creditCardService.showAll();
    }

    // Get all data from specific Credit Card account, by id. Only admins can do this:
    @GetMapping("/admin/credit-card/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<CreditCard> find(@PathVariable Long id) {
        return creditCardService.find(id);
    }
}
