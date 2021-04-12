package com.codegym.restaurant.exception;


public class ShiftNotFoundException extends RuntimeException {


    public static final String ERROR_CODE = "Shift.NotFound";

    public ShiftNotFoundException(String message) {
        super(message);
    }
}
