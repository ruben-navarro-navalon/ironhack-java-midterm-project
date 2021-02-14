package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.classes.Address;
import com.ironhack.midtermProject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermProject.enums.State;
import com.ironhack.midtermProject.enums.UserRole;
import com.ironhack.midtermProject.model.AccountHolder;
import com.ironhack.midtermProject.model.Role;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.RoleRepository;
import com.ironhack.midtermProject.service.interfaces.IAccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountHolderService implements IAccountHolderService {
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    RoleRepository roleRepository;

    // Create new Account Holder:
    public AccountHolder create(AccountHolderDTO accountHolderDTO) {
        AccountHolder accountHolder = new AccountHolder();
        accountHolder.setName(accountHolderDTO.getName());
        accountHolder.setUsername(accountHolderDTO.getUsername());
        accountHolder.setPassword(accountHolderDTO.getPassword());
        accountHolder.setBirthDate(accountHolderDTO.getBirthDate());
        Address primaryAddress = new Address();

        // Check if State in DTO (string) is a valid one from State Enum:
        try {
            primaryAddress.setState(State.valueOf(accountHolderDTO.getPrimaryAddressDTO().getState().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(accountHolderDTO.getPrimaryAddressDTO().getState() + " is not a valid State.");
        }
        primaryAddress.setDirection(accountHolderDTO.getPrimaryAddressDTO().getDirection());
        primaryAddress.setPostalCode(accountHolderDTO.getPrimaryAddressDTO().getPostalCode());
        primaryAddress.setCity(accountHolderDTO.getPrimaryAddressDTO().getCity());
        accountHolder.setPrimaryAddress(primaryAddress);

        // If DTO has mailing address:
        if (accountHolderDTO.getMailingAddressDTO() != null){
            Address mailingAddress = new Address();

            // Check if State in DTO (string) is a valid one from State Enum:
            try {
                mailingAddress.setState(State.valueOf(accountHolderDTO.getMailingAddressDTO().getState().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(accountHolderDTO.getMailingAddressDTO().getState() + " is not a valid State.");
            }
            mailingAddress.setDirection(accountHolderDTO.getMailingAddressDTO().getDirection());
            mailingAddress.setPostalCode(accountHolderDTO.getMailingAddressDTO().getPostalCode());
            mailingAddress.setCity(accountHolderDTO.getMailingAddressDTO().getCity());
            accountHolder.setMailingAddress(mailingAddress);
        }

        accountHolder = accountHolderRepository.save(accountHolder);
        Role role = roleRepository.save(new Role(UserRole.ACCOUNT_HOLDER, accountHolder));
        return accountHolder;
    }
}
