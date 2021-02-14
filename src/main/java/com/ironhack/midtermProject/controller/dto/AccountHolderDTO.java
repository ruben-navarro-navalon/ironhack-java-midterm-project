package com.ironhack.midtermProject.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

public class AccountHolderDTO extends UserDTO {
    // Properties:
    @NotNull(message = "Date of birth can't be null.")
    @Past(message = "Wrong Date of birth.")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;

    @NotNull(message = "Primary Address can't be null.")
    private AddressDTO primaryAddressDTO;

    private AddressDTO mailingAddressDTO;

    // Constructors:
    public AccountHolderDTO() {
    }
    public AccountHolderDTO(@NotNull(message = "Name can't be null.") @NotEmpty(message = "Name can't be empty.") String name, @NotNull(message = "Username can't be null.") @NotEmpty(message = "Username can't be empty.") String username, @NotNull(message = "Password can't be null.") @NotEmpty(message = "Password can't be empty.") String password, @NotNull(message = "Date of birth can't be null.") @Past(message = "Wrong Date of birth.") LocalDate birthDate, @NotEmpty(message = "Primary Address can't be empty.") @NotNull(message = "Primary Address can't be null.") AddressDTO primaryAddressDTO) {
        super(name, username, password);
        setBirthDate(birthDate);
        setPrimaryAddressDTO(primaryAddressDTO);
    }
    public AccountHolderDTO(@NotNull(message = "Name can't be null.") @NotEmpty(message = "Name can't be empty.") String name, @NotNull(message = "Username can't be null.") @NotEmpty(message = "Username can't be empty.") String username, @NotNull(message = "Password can't be null.") @NotEmpty(message = "Password can't be empty.") String password, @NotNull(message = "Date of birth can't be null.") @Past(message = "Wrong Date of birth.") LocalDate birthDate, @NotEmpty(message = "Primary Address can't be empty.") @NotNull(message = "Primary Address can't be null.") AddressDTO primaryAddressDTO, AddressDTO mailingAddressDTO) {
        this(name, username, password, birthDate, primaryAddressDTO);
        setMailingAddressDTO(mailingAddressDTO);
    }

    // Getters & Setters:
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public AddressDTO getPrimaryAddressDTO() {
        return primaryAddressDTO;
    }
    public void setPrimaryAddressDTO(AddressDTO primaryAddressDTO) {
        this.primaryAddressDTO = primaryAddressDTO;
    }
    public AddressDTO getMailingAddressDTO() {
        return mailingAddressDTO;
    }
    public void setMailingAddressDTO(AddressDTO mailingAddressDTO) {
        this.mailingAddressDTO = mailingAddressDTO;
    }
}
