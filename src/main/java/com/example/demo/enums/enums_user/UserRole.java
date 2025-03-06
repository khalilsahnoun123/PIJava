package com.example.demo.enums.enums_user;

public enum UserRole {
    ADMIN, COVOITUREUR, CHAUFFEUR_TAXI, USER,VOYAGEUR ;

    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Return null if the role is invalid
        }
    }
}
