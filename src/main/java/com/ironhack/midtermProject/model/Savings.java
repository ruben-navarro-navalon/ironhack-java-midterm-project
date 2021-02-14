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
public class Savings extends Account{
    // Properties:
    private String secretKey;
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Column(columnDefinition = "DECIMAL(5,4)")
    private BigDecimal interestRate;
    private LocalDate interestUpdate;
    private Boolean belowMinimumBalance;

    private static final Money DEFAULT_MINIMUM_BALANCE = new Money(new BigDecimal("1000"));
    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.0025");

    // Constructors:
    public Savings() {
        setMinimumBalance(DEFAULT_MINIMUM_BALANCE);
        setInterestRate(DEFAULT_INTEREST_RATE);
        setStatus(Status.ACTIVE);
        setBelowMinimumBalance(false);
        setInterestUpdate(LocalDate.now());
    }
    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money minimumBalance, BigDecimal interestRate, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setMinimumBalance(minimumBalance);
        setInterestRate(interestRate);
        setSecretKey(secretKey);
        setStatus(Status.ACTIVE);
        setBelowMinimumBalance(false);
        setInterestUpdate(LocalDate.now());
    }
    public Savings(Money balance, AccountHolder primaryOwner, Money minimumBalance, BigDecimal interestRate, String secretKey) {
        this(balance, primaryOwner, null, minimumBalance, interestRate, secretKey);
    }
    public Savings(Money balance, AccountHolder primaryOwner, Money minimumBalance, String secretKey) {
        this(balance, primaryOwner, null, minimumBalance, DEFAULT_INTEREST_RATE, secretKey);
    }
    public Savings(Money balance, AccountHolder primaryOwner, String secretKey) {
        this(balance, primaryOwner, null, DEFAULT_MINIMUM_BALANCE, DEFAULT_INTEREST_RATE, secretKey);
    }
    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        this(balance, primaryOwner, secondaryOwner, DEFAULT_MINIMUM_BALANCE, DEFAULT_INTEREST_RATE, secretKey);
    }
    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money minimumBalance, String secretKey) {
        this(balance, primaryOwner, secondaryOwner, minimumBalance, DEFAULT_INTEREST_RATE, secretKey);
    }
    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, String secretKey) {
        this(balance, primaryOwner, secondaryOwner, DEFAULT_MINIMUM_BALANCE, interestRate, secretKey);
    }
    public Savings(Money balance, AccountHolder primaryOwner, BigDecimal interestRate, String secretKey) {
        this(balance, primaryOwner, null, DEFAULT_MINIMUM_BALANCE, interestRate, secretKey);
    }

    // Getters & Setters:
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.secretKey = passwordEncoder.encode(secretKey);
    }
    public Money getMinimumBalance() {
        return minimumBalance;
    }
    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public LocalDate getInterestUpdate() {
        return interestUpdate;
    }
    public void setInterestUpdate(LocalDate interestUpdate) {
        this.interestUpdate = interestUpdate;
    }
    public Boolean getBelowMinimumBalance() {
        return belowMinimumBalance;
    }
    public void setBelowMinimumBalance(Boolean belowMinimumBalance) {
        this.belowMinimumBalance = belowMinimumBalance;
    }
}
