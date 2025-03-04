package com.example.demo.controllers.controllers_TP;

import com.example.demo.models.User;
import com.example.demo.models.User;
import com.example.demo.enums.UserRole;

public class UserSession {
    private static UserSession instance;
    private User loggedInUser;

    private UserSession(User user) {
        this.loggedInUser = user;
    }

    public static UserSession getInstance(User user) {
        if (instance == null) {
            instance = new UserSession(user);
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public UserRole getUserRole() {
        return loggedInUser != null ? loggedInUser.getRole() : null;
    }

    public boolean isAdmin() {
        return loggedInUser != null && loggedInUser.getRole() == UserRole.ADMIN;
    }

    public boolean isUser() {
        return loggedInUser != null && loggedInUser.getRole() == UserRole.USER;
    }

    public boolean isCovoitureur() {
        return loggedInUser != null && loggedInUser.getRole() == UserRole.COVOITUREUR;
    }

    public boolean isChauffeurTaxi() {
        return loggedInUser != null && loggedInUser.getRole() == UserRole.CHAUFFEUR_TAXI;
    }

    public boolean isVoyageur() {
        return loggedInUser != null && loggedInUser.getRole() == UserRole.VOYAGEUR;
    }

    public void logout() {
        loggedInUser = null;
        instance = null;
    }
}
