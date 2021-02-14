package com.ironhack.midtermProject.controller.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AdminDTO extends UserDTO{
    public AdminDTO() {
    }
    public AdminDTO(@NotNull(message = "Name can't be null.") @NotEmpty(message = "Name can't be empty.") String name, @NotNull(message = "Username can't be null.") @NotEmpty(message = "Username can't be empty.") String username, @NotNull(message = "Password can't be null.") @NotEmpty(message = "Password can't be empty.") String password) {
        super(name, username, password);
    }
}
