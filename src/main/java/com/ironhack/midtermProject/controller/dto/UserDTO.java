package com.ironhack.midtermProject.controller.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDTO {
    @NotNull(message = "Name can't be null.")
    @NotEmpty(message = "Name can't be empty.")
    private String name;
    @NotNull(message = "Username can't be null.")
    @NotEmpty(message = "Username can't be empty.")
    private String username;
    @NotNull(message = "Password can't be null.")
    @NotEmpty(message = "Password can't be empty.")
    private String password;

    public UserDTO() {
    }
    public UserDTO(@NotNull(message = "Name can't be null.") @NotEmpty(message = "Name can't be empty.") String name, @NotNull(message = "Username can't be null.") @NotEmpty(message = "Username can't be empty.") String username, @NotNull(message = "Password can't be null.") @NotEmpty(message = "Password can't be empty.") String password) {
        setName(name);
        setUsername(username);
        setPassword(password);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
