package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermProject.model.AccountHolder;

public interface IAccountHolderController {

    // The methods are described in the controller implementation.

    public AccountHolder create(AccountHolderDTO accountHolderDTO);
}
