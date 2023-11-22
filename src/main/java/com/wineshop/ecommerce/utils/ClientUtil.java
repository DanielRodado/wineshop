package com.wineshop.ecommerce.utils;

public class ClientUtil {

    public static boolean isValidEmail(String email) throws IllegalArgumentException {

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)\\.[A-Za-z]{1,}$\n")) {
            throw new IllegalArgumentException("Email is invalid");
        }

        return true;
    }

    public static boolean isValidPassword(String password) throws IllegalArgumentException {

        if (!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,15}$")) {
            throw new IllegalArgumentException("Password is invalid, make sure to match the requirements");
        }

        return true;
    }
}
