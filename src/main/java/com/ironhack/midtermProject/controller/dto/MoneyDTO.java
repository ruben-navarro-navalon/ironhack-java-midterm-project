package com.ironhack.midtermProject.controller.dto;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class MoneyDTO {
    @DecimalMin(value = "0.00", message = "Min Amount is 0.00.")
    private BigDecimal amount;

    public MoneyDTO() {
    }
    public MoneyDTO(@DecimalMin(value = "0.00", message = "Min Amount is 0.00.") BigDecimal amount) {
        setAmount(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
