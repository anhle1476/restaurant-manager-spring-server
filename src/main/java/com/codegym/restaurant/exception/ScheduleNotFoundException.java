package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScheduleNotFoundException extends RuntimeException {
    public static final String ERROR_CODE = "Schedule.NotFound";

    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
