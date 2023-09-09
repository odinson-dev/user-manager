package com.convrse.accountmanagerlib.exceptions;

public class DuplicateAppIdException extends RuntimeException {
    public DuplicateAppIdException(String message) {
        this(message, (Throwable)null);
    }

    public DuplicateAppIdException(String message, Throwable cause) {
        super(message, cause);
    }
}