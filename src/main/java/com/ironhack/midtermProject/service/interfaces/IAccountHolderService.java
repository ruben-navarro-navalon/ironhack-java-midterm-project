package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermProject.model.AccountHolder;

public interface IAccountHolderService {

    // The methods are described in the service implementation.

    AccountHolder create(AccountHolderDTO accountHolderDTO);
}
