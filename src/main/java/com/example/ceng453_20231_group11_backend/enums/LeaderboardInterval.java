package com.example.ceng453_20231_group11_backend.enums;

public enum LeaderboardInterval {
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY");

    private final String displayName;

    LeaderboardInterval(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

}
