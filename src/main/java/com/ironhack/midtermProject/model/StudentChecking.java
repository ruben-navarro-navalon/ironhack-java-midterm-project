package com.ironhack.midtermProject.model;

import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.enums.Status;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class StudentChecking extends Account{
    // Properties:
    private String secretKey;
    @Enumerated(value = EnumType.STRING)
    private Status status;

    // Constructors:
    public StudentChecking(){
        setStatus(Status.ACTIVE);
    }
    public StudentChecking(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
        setStatus(Status.ACTIVE);      // It is created as ACTIVE.
    }
    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
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
}
