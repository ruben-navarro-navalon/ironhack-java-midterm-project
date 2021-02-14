package com.ironhack.midtermProject.model;

import com.ironhack.midtermProject.enums.UserRole;

import javax.persistence.*;

@Entity
public class Role {
    // Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;
    @ManyToOne
    private User user;

    // Constructors:
    public Role() {
    }
    public Role(UserRole userRole, User user) {
        setUserRole(userRole);
        setUser(user);
    }

    // Getters & Setters:
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public UserRole getUserRole() {
        return userRole;
    }
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
