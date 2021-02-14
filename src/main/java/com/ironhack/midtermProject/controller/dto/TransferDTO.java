package com.ironhack.midtermProject.controller.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferDTO {
    // Properties:
    @NotNull(message = "Origin Account can't be null.")
    private Long originAccountId;
    @NotNull(message = "Destination Account can't be null.")
    private Long destinationAccountId;
    @NotNull(message = "Destination Name can't be null.")
    @NotEmpty(message = "Destination Name can't be empty.")
    private String destinationName;
    private String concept;
    @NotNull(message = "Transfer amount can't be null.")
    @DecimalMin(value = "0.01", message = "Transfer min amount is 0.01.")
    private BigDecimal transferAmount;

    // Constructors:
    public TransferDTO() {
    }
    public TransferDTO(@NotNull(message = "Origin Account can't be null.") Long originAccountId, @NotNull(message = "Destination Account can't be null.") Long destinationAccountId, @NotNull(message = "Destination Name can't be null.") @NotEmpty(message = "Destination Name can't be empty.") String destinationName, @NotNull(message = "Transfer amount can't be null.") @DecimalMin(value = "0.01", message = "Transfer min amount is 0.01.") BigDecimal transferAmount) {
        setOriginAccountId(originAccountId);
        setDestinationAccountId(destinationAccountId);
        setDestinationName(destinationName);
        setTransferAmount(transferAmount);
    }
    public TransferDTO(@NotNull(message = "Origin Account can't be null.") Long originAccountId, @NotNull(message = "Destination Account can't be null.") Long destinationAccountId, @NotNull(message = "Destination Name can't be null.") @NotEmpty(message = "Destination Name can't be empty.") String destinationName, String concept, @NotNull(message = "Transfer amount can't be null.") @DecimalMin(value = "0.01", message = "Transfer min amount is 0.01.") BigDecimal transferAmount) {
        this(originAccountId, destinationAccountId, destinationName, transferAmount);
        setConcept(concept);
    }

    // Getters & Setters:
    public Long getOriginAccountId() {
        return originAccountId;
    }
    public void setOriginAccountId(Long originAccountId) {
        this.originAccountId = originAccountId;
    }
    public Long getDestinationAccountId() {
        return destinationAccountId;
    }
    public void setDestinationAccountId(Long destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }
    public String getConcept() {
        return concept;
    }
    public void setConcept(String concept) {
        this.concept = concept;
    }
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }
    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
    public String getDestinationName() {
        return destinationName;
    }
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
}
