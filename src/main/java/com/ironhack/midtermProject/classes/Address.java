package com.ironhack.midtermProject.classes;

import com.ironhack.midtermProject.enums.State;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Address {
    // Properties:
    private String direction;
    private int postalCode;
    private String city;
    @Enumerated(value = EnumType.STRING)
    private State state;   // I only consider users from Spain for the purposes of this project.

    // Constructors:
    public Address() {
    }
    public Address(String direction, int postalCode, String city, State state) {
        setDirection(direction);
        setPostalCode(postalCode);
        setCity(city);
        setState(state);
    }

    // Getters & Setters:
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public int getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
}
