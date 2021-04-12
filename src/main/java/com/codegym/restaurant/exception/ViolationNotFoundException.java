package com.codegym.restaurant.exception;

public class ViolationNotFoundException extends RuntimeException{

    public static final String ERROR_CODE = "Violation.NotFound";

    public ViolationNotFoundException(String message) {
        super(message);
    }
}
