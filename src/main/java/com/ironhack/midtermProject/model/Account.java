package com.ironhack.midtermProject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.midtermProject.classes.Money;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Account {
    // Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    @AttributeOverrides(value = {
        @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "balance_currency"))
    })
    private Money balance;
    @ManyToOne
    private AccountHolder primaryOwner;
    @ManyToOne
    private AccountHolder secondaryOwner;
    @Embedded
    @AttributeOverrides(value = {
        @AttributeOverride(name = "amount", column = @Column(name = "penalty_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "penalty_currency"))
    })
    private Money penaltyFee;
    private LocalDate creationDate;

    @OneToMany(mappedBy = "originAccount")
    @JsonIgnore
    private List<Transfer> sentMoney;
    @OneToMany(mappedBy = "destinationAccount")
    @JsonIgnore
    private List<Transfer> receivedMoney;

    // Constructors:
    public Account() {
        setPenaltyFee();
        setCreationDate(LocalDate.now());
    }
    public Account(Money balance, AccountHolder primaryOwner) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setPenaltyFee();
        setCreationDate(LocalDate.now());
    }
    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this(balance, primaryOwner);
        setSecondaryOwner(secondaryOwner);
    }

    // Getters & Setters:
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Money getBalance() {
        return balance;
    }
    public void setBalance(Money balance) {
        this.balance = balance;
    }
    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }
    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }
    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }
    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }
    public Money getPenaltyFee() {
        return penaltyFee;
    }
    public void setPenaltyFee() {
        this.penaltyFee = new Money(new BigDecimal("40"));
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    public List<Transfer> getSentMoney() {
        return sentMoney;
    }
    public void setSentMoney(List<Transfer> sentMoney) {
        this.sentMoney = sentMoney;
    }
    public List<Transfer> getReceivedMoney() {
        return receivedMoney;
    }
    public void setReceivedMoney(List<Transfer> receivedMoney) {
        this.receivedMoney = receivedMoney;
    }
}
