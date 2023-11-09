package com.example.ceng453_20231_group11_backend.enums;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}