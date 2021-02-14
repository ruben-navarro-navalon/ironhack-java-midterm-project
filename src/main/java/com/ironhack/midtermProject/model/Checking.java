package com.ironhack.midtermProject.model;

import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.enums.Status;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Checking extends Account{
    // Properties:
    private String secretKey;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance;
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "monthly_maintenance_fee_currency"))
    })
    private Money monthlyMaintenanceFee;
    private LocalDate maintenanceUpdate;
    private Boolean belowMinimumBalance;

    // Constructors:
    public Checking() {
        setStatus(Status.ACTIVE);
        setMonthlyMaintenanceFee();
        setMaintenanceUpdate(LocalDate.now());
        setMinimumBalance();
        setBelowMinimumBalance(false);
    }
    public Checking(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
        setStatus(Status.ACTIVE);
        setMonthlyMaintenanceFee();
        setMaintenanceUpdate(LocalDate.now());
        setMinimumBalance();
        setBelowMinimumBalance(false);
    }
    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        this(balance, primaryOwner, secretKey);
        setSecondaryOwner(secondaryOwner);
    }

    // Getters & Setters:
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.secretKey = passwordEncoder.encode(secretKey);
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Money getMinimumBalance() {
        return minimumBalance;
    }
    public void setMinimumBalance() {
        this.minimumBalance = new Money(new BigDecimal("250"));
    }
    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }
    public void setMonthlyMaintenanceFee() {
        this.monthlyMaintenanceFee = new Money(new BigDecimal("12"));
    }
    public Boolean getBelowMinimumBalance() {
        return belowMinimumBalance;
    }
    public void setBelowMinimumBalance(Boolean belowMinimumBalance) {
        this.belowMinimumBalance = belowMinimumBalance;
    }
    public LocalDate getMaintenanceUpdate() {
        return maintenanceUpdate;
    }
    public void setMaintenanceUpdate(LocalDate maintenanceUpdate) {
        this.maintenanceUpdate = maintenanceUpdate;
    }
}
