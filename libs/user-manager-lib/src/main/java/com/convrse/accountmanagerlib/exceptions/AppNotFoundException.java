package com.convrse.accountmanagerlib.exceptions;

public class AppNotFoundException extends RuntimeException {
    public AppNotFoundException(String message) {
        this(message, (Throwable)null);
    }

    public AppNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}