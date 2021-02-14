package com.ironhack.midtermProject.enums;

public enum Status {
    ACTIVE("Active"),
    FROZEN("Frozen");

    private final String NAME;

    Status(String name) {
        this.NAME = name;
    }

    public String getNAME() {
        return NAME;
    }
}
