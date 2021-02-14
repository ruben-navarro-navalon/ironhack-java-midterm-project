package com.ironhack.midtermProject.controller.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ThirdPartyDTO{
    // Properties:
    @NotNull(message = "Name can't be null.")
    @NotEmpty(message = "Name can't be empty.")
    private String name;
    @NotNull(message = "Name can't be null.")
    @NotEmpty(message = "Name can't be empty.")
    private String hashKey;

    // Constructors:
    public ThirdPartyDTO() {
    }
    public ThirdPartyDTO(@NotNull(message = "Name can't be null.") @NotEmpty(message = "Name can't be empty.") String name, @NotNull(message = "Name can't be null.") @NotEmpty(message = "Name can't be empty.") String hashKey) {
        setName(name.trim());
        setHashKey(hashKey.trim());
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getHashKey() {
        return hashKey;
    }
    public void setHashKey(String hashKey) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.hashKey = passwordEncoder.encode(hashKey);
    }
}
