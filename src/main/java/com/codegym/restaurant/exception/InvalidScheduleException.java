package com.codegym.restaurant.exception;


public class InvalidScheduleException extends RuntimeException {

    public static final String ERROR_CODE = "Schedule.Invalid";

    public InvalidScheduleException(String message) {
        super(message);
    }
}
