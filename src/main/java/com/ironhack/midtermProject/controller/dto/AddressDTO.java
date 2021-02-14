package com.ironhack.midtermProject.controller.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AddressDTO {
    // Properties:
    @NotEmpty(message = "Direction can't be empty.")
    @NotNull(message = "Direction can't be null.")
    private String direction;
    @NotNull(message = "Postal Code can't be null.")
    @Digits(integer = 5, fraction = 0, message = "Postal Code has to be 5 digits long.")
    private int postalCode;
    @NotEmpty(message = "City can't be empty.")
    @NotNull(message = "City can't be null.")
    private String city;
    @NotEmpty(message = "State can't be empty.")
    @NotNull(message = "State can't be null.")
    private String state;

    // Constructors:
    public AddressDTO() {
    }
    public AddressDTO(@NotEmpty(message = "Direction can't be empty.") @NotNull(message = "Direction can't be null.") String direction, @NotNull(message = "Postal Code can't be null.") @Digits(integer = 5, fraction = 0, message = "Postal Code has to be 5 digits long.") int postalCode, @NotEmpty(message = "City can't be empty.") @NotNull(message = "City can't be null.") String city, @NotEmpty(message = "State can't be empty.") @NotNull(message = "State can't be null.") String state) {
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
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
