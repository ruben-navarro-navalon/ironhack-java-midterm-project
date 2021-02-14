package com.ironhack.midtermProject.controller.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class SavingsDTO extends AccountDTO{
    // Properties:
    @NotNull(message = "Secret Key can't be null.")
    @NotEmpty(message = "Secret Key can't be empty.")
    private String secretKey;
    @DecimalMax(value = "1000", message = "Minimum balance has to be below 1000.")
    @DecimalMin(value = "100", message = "Minimum balance has to be over 100.")
    private BigDecimal minimumBalance;
    @DecimalMax(value = "0.5", message = "Max Interest Rate is 0.5.")
    @DecimalMin(value = "0.0025", message = "Min Interest Rate is 0.0025.")
    private BigDecimal interestRate;

    public SavingsDTO() {
    }

    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.secretKey = passwordEncoder.encode(secretKey);
    }
    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }
    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
