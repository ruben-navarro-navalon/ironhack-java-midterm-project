package com.ironhack.midtermProject.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ironhack.midtermProject.classes.Money;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transfer {
    // Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "origin_account_id")
    private Account originAccount;
    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;
    private String destinationName;
    private String concept;
    private Money amount;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;

    // Constructors:
    public Transfer() {
        setDate(LocalDateTime.now());
    }
    public Transfer(Account originAccount, Account destinationAccount, String destinationName, Money amount) {
        setOriginAccount(originAccount);
        setDestinationAccount(destinationAccount);
        setDestinationName(destinationName);
        setAmount(amount);
        setDate(LocalDateTime.now());
    }
    public Transfer(Account originAccount, Account destinationAccount, String destinationName, String concept, Money amount) {
        this(originAccount, destinationAccount, destinationName, amount);
        setConcept(concept);
    }

    // Getters & Setters:
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Account getOriginAccount() {
        return originAccount;
    }
    public void setOriginAccount(Account originAccount) {
        this.originAccount = originAccount;
    }
    public Account getDestinationAccount() {
        return destinationAccount;
    }
    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    public String getConcept() {
        return concept;
    }
    public void setConcept(String concept) {
        this.concept = concept;
    }
    public Money getAmount() {
        return amount;
    }
    public void setAmount(Money amount) {
        this.amount = amount;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getDestinationName() {
        return destinationName;
    }
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
}
