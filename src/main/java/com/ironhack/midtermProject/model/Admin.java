package com.ironhack.midtermProject.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class Admin extends User{
    // Constructors:
    public Admin() {
    }
    public Admin(@NotNull String name, @NotNull String username, @NotNull String password) {
        super(name, username, password);
    }
}
