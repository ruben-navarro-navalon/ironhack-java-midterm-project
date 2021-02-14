package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.CreditCardDTO;
import com.ironhack.midtermProject.model.CreditCard;

import java.util.List;
import java.util.Optional;

public interface ICreditCardService {

    // The methods are described in the service implementation.

    public CreditCard create(CreditCardDTO creditCardDTO);
    public List<CreditCard> showAll();
    public Optional<CreditCard> find(Long id);
    public void updateInterest(CreditCard creditCard);
}
