package com.ironhack.midtermProject.model;

import com.ironhack.midtermProject.classes.Money;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends Account{
    // Properties:
    @Column(columnDefinition = "DECIMAL(5,4)")
    private BigDecimal interestRate;
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency"))
    })
    private Money creditLimit;
    private LocalDate interestUpdate;

    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.2");
    private static final Money DEFAULT_CREDIT_LIMIT = new Money(new BigDecimal("100"));

    // Constructors:
    public CreditCard() {
        setInterestRate(DEFAULT_INTEREST_RATE);
        setCreditLimit(DEFAULT_CREDIT_LIMIT);
        setInterestUpdate(LocalDate.now());
    }
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, Money creditLimit) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setCreditLimit(creditLimit);
        setInterestUpdate(LocalDate.now());
    }
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate) {
        this(balance, primaryOwner, secondaryOwner, interestRate, DEFAULT_CREDIT_LIMIT);
    }
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit) {
        this(balance, primaryOwner, secondaryOwner, DEFAULT_INTEREST_RATE, creditLimit);
    }
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this(balance, primaryOwner, secondaryOwner, DEFAULT_INTEREST_RATE, DEFAULT_CREDIT_LIMIT);
    }
    public CreditCard(Money balance, AccountHolder primaryOwner, BigDecimal interestRate, Money creditLimit) {
        this(balance, primaryOwner, null, interestRate, creditLimit);
    }
    public CreditCard(Money balance, AccountHolder primaryOwner, BigDecimal interestRate) {
        this(balance, primaryOwner, null, interestRate, DEFAULT_CREDIT_LIMIT);
    }
    public CreditCard(Money balance, AccountHolder primaryOwner, Money creditLimit) {
        this(balance, primaryOwner, null, DEFAULT_INTEREST_RATE, creditLimit);
    }
    public CreditCard(Money balance, AccountHolder primaryOwner) {
        this(balance, primaryOwner, null, DEFAULT_INTEREST_RATE, DEFAULT_CREDIT_LIMIT);
    }

    // Getters & Setters:
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public Money getCreditLimit() {
        return creditLimit;
    }
    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }
    public LocalDate getInterestUpdate() {
        return interestUpdate;
    }
    public void setInterestUpdate(LocalDate interestUpdate) {
        this.interestUpdate = interestUpdate;
    }
}
