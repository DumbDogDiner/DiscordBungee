package com.dumbdogdiner.discordbungee;

public class DatabaseError extends Exception {
    public DatabaseError(String errorMessage) {
        super(errorMessage);
    }
}
