package com.andersen.authorization;

import com.andersen.authorization.models.User;

import java.util.Scanner;

import static java.lang.System.out;

public class Authenticator {

    private static final Authenticator instance = new Authenticator();

    public static Authenticator getInstance() {
        return instance;
    }

    private boolean isAuthenticated;
    private User user;

    private Authenticator() {
        this.isAuthenticated = false;
        this.user = new User(1L, "root", "pass");
    }

    public void authenticate(Scanner scanner) {
        out.print("Your login: ");
        String login = scanner.nextLine();
        if (!user.getLogin().equals(login)) throw new IllegalArgumentException("User not found");
        out.print("Password: ");
        String password = new String(scanner.nextLine());
        if (!user.getPassword().equals(password)) throw new IllegalArgumentException("Wrong password");
        isAuthenticated = true;
    }

    public User getUser() {
        return user;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
