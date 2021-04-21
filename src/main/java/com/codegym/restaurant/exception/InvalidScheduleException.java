package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidScheduleException extends RuntimeException {

    public static final String ERROR_CODE = "Schedule.Invalid";

    public InvalidScheduleException(String message) {
        super(message);
    }
}
