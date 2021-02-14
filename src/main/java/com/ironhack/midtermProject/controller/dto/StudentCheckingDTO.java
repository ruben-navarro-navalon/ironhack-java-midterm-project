package com.ironhack.midtermProject.controller.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class StudentCheckingDTO extends AccountDTO{
    // Properties:
    @NotNull(message = "Secret Key can't be null.")
    @NotEmpty(message = "Secret Key can't be empty.")
    private String secretKey;

    // Constructors:
    public StudentCheckingDTO() {
    }
    public StudentCheckingDTO(@NotNull(message = "Balance can't be null.") BigDecimal balance, @NotNull(message = "Owner can't be null.") Long primaryOwnerId, @NotNull(message = "Secret Key can't be null.") @NotEmpty(message = "Secret Key can't be empty.") String secretKey) {
        super(balance, primaryOwnerId);
        setSecretKey(secretKey);
    }
    public StudentCheckingDTO(@NotNull(message = "Balance can't be null.") BigDecimal balance, @NotNull(message = "Owner can't be null.") Long primaryOwnerId, Long secondaryOwnerId, @NotNull(message = "Secret Key can't be null.") @NotEmpty(message = "Secret Key can't be empty.") String secretKey) {
        this(balance, primaryOwnerId, secretKey);
        setSecondaryOwnerId(secondaryOwnerId);
    }

    // Getters & Setters:
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.secretKey = passwordEncoder.encode(secretKey);
    }
}
