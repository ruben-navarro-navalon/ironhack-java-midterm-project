package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.CreditCardDTO;
import com.ironhack.midtermProject.model.CreditCard;

import java.util.List;
import java.util.Optional;

public interface ICreditCardController {

    // The methods are described in the controller implementation.

    public CreditCard create(CreditCardDTO creditCardDTO);
    public List<CreditCard> showAll();
    public Optional<CreditCard> find(Long id);
}
