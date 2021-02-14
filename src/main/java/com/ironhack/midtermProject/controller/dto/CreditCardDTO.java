package com.ironhack.midtermProject.controller.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class CreditCardDTO extends AccountDTO{
    // Properties:
    @DecimalMax(value = "100000", message = "Max Credit Limit is 100000.")
    @DecimalMin(value = "100", message = "Min Credit Limit is 100.")
    private BigDecimal creditLimit;
    @DecimalMax(value = "0.2", message = "Max Interest Rate is 0.2")
    @DecimalMin(value = "0.1", message = "Min Interest Rate is 0.1")
    private BigDecimal interestRate;

    // Constructor:
    public CreditCardDTO() {
    }

    // Getters & Setters:
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
