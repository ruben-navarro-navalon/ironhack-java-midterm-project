package com.ironhack.midtermProject.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ThirdParty {
    // Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String hashKey;

    // Constructors:
    public ThirdParty() {
    }
    public ThirdParty(String name,String hashKey) {
        setName(name.trim());
        setHashKey(hashKey.trim());
    }

    // Getters & Setters:
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getHashKey() {
        return hashKey;
    }
    public void setHashKey(String hashKey) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.hashKey = passwordEncoder.encode(hashKey);
    }
}
