package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.controller.dto.MoneyDTO;
import com.ironhack.midtermProject.controller.dto.TransferDTO;
import com.ironhack.midtermProject.model.Transfer;

import java.security.Principal;

public interface IAccountController {

    // The methods are described in the controller implementation.

    public Money checkBalance(Long id, Principal principal);
    public Transfer transfer(TransferDTO transferDTO, Principal principal);
    public Transfer transferToThirdParty(TransferDTO transferDTO, String hashKey, String secretKey);
    public Transfer transferFromThirdParty(TransferDTO transferDTO, String hashKey, String secretKey);
    public void modifyBalance(Long id, MoneyDTO newBalance);
}
