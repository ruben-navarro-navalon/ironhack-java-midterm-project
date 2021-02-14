package com.ironhack.midtermProject.controller.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AccountDTO {
    // Properties:
    @NotNull(message = "Balance can't be null.")
    @DecimalMin(value = "0.00", message = "Balance can't be negative.")
    private BigDecimal balance;
    @NotNull(message = "Owner can't be null.")
    private Long primaryOwnerId;
    private Long secondaryOwnerId;

    // Constructors:
    public AccountDTO() {
    }
    public AccountDTO(@NotNull(message = "Balance can't be null.") @DecimalMin(value = "0.00", message = "Balance can't be negative.") BigDecimal balance, @NotNull(message = "Owner can't be null.") Long primaryOwnerId) {
        setBalance(balance);
        setPrimaryOwnerId(primaryOwnerId);
    }
    public AccountDTO(@NotNull(message = "Balance can't be null.") @DecimalMin(value = "0.00", message = "Balance can't be negative.") BigDecimal balance, @NotNull(message = "Owner can't be null.") Long primaryOwnerId, Long secondaryOwnerId) {
        this(balance, primaryOwnerId);
        setSecondaryOwnerId(secondaryOwnerId);
    }

    // Getters & Setters:
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public Long getPrimaryOwnerId() {
        return primaryOwnerId;
    }
    public void setPrimaryOwnerId(Long primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }
    public Long getSecondaryOwnerId() {
        return secondaryOwnerId;
    }
    public void setSecondaryOwnerId(Long secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
    }
}
