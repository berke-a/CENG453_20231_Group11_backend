package com.example.ceng453_20231_group11_backend.enums;

public enum GameState {
    WAITING("WAITING"),
    STARTED("STARTED"),
    FINISHED("FINISHED");

    private final String displayName;

    GameState(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String toString() {
        return this.displayName;
    }
}
