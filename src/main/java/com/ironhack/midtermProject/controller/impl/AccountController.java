package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.controller.dto.MoneyDTO;
import com.ironhack.midtermProject.controller.dto.TransferDTO;
import com.ironhack.midtermProject.controller.interfaces.IAccountController;
import com.ironhack.midtermProject.model.Transfer;
import com.ironhack.midtermProject.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class AccountController implements IAccountController {
    @Autowired
    IAccountService accountService;

    // Check balance by account id. Only admins or owners can check it:
    @GetMapping("/check-balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money checkBalance(@PathVariable Long id, Principal principal) {
        return accountService.checkBalance(id, principal);
    }

    // Create new transfer between accounts owned by Account Holders. Only owner of origin account cant do this:
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer transfer(@RequestBody @Valid TransferDTO transferDTO, Principal principal) {
        return accountService.transfer(transferDTO, principal);
    }

    // Create new transfer from Account Holder to ThirdParty:
    @PostMapping("/transfer-to-third-party/{hashKey}/{secretKey}")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer transferToThirdParty(@RequestBody @Valid TransferDTO transferDTO, @PathVariable String hashKey, @PathVariable String secretKey) {
        return accountService.transferToThirdParty(transferDTO, hashKey, secretKey);
    }

    // Create new transfer from Third Party to Account Holder:
    @PostMapping("/transfer-from-third-party/{hashKey}/{secretKey}")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer transferFromThirdParty(@RequestBody @Valid TransferDTO transferDTO, @PathVariable String hashKey, @PathVariable String secretKey) {
        return accountService.transferFromThirdParty(transferDTO, hashKey, secretKey);
    }


    // Modify balance by account id. Only admins can do this:
    @PatchMapping("/admin/modify-balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyBalance(@PathVariable Long id, @RequestBody @Valid MoneyDTO newBalance) {
        accountService.modifyBalance(id, newBalance);
    }
}
