package com.convrse.accountmanagerlib.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        this(message, (Throwable)null);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}