package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ViolationNotFoundException extends RuntimeException{

    public static final String ERROR_CODE = "Violation.NotFound";

    public ViolationNotFoundException(String message) {
        super(message);
    }
}
