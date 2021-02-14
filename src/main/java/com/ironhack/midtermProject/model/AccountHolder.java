package com.ironhack.midtermProject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.midtermProject.classes.Address;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AccountHolder extends User{
    // Properties:
    private LocalDate birthDate;
    @Embedded
    @AttributeOverrides(value = {
        @AttributeOverride(name = "direction", column = @Column(name = "primary_address_direction")),
        @AttributeOverride(name = "postalCode", column = @Column(name = "primary_address_postal_code")),
        @AttributeOverride(name = "city", column = @Column(name = "primary_address_city")),
        @AttributeOverride(name = "state", column = @Column(name = "primary_address_state"))
    })
    private Address primaryAddress;
    @Embedded
    @AttributeOverrides(value = {
        @AttributeOverride(name = "direction", column = @Column(name = "mailing_address_direction")),
        @AttributeOverride(name = "postalCode", column = @Column(name = "mailing_address_postal_code")),
        @AttributeOverride(name = "city", column = @Column(name = "mailing_address_city")),
        @AttributeOverride(name = "state", column = @Column(name = "mailing_address_state"))
    })
    private Address mailingAddress;
    @OneToMany(mappedBy = "primaryOwner")
    @JsonIgnore
    private List<Account> primaryAccounts;
    @OneToMany(mappedBy = "secondaryOwner")
    @JsonIgnore
    private List<Account> secondaryAccounts;

    // Constructors:
    public AccountHolder() {
    }
    public AccountHolder(String name, String username, String password, LocalDate birthDate, Address primaryAddress) {
        super(name, username, password);
        setBirthDate(birthDate);
        setPrimaryAddress(primaryAddress);
    }
    public AccountHolder(String name, String username, String password, LocalDate birthDate, Address primaryAddress, Address mailingAddress) {
        this(name, username, password, birthDate, primaryAddress);
        setMailingAddress(mailingAddress);
    }

    // Getters & Setters:
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public Address getPrimaryAddress() {
        return primaryAddress;
    }
    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }
    public Address getMailingAddress() {
        return mailingAddress;
    }
    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
}
