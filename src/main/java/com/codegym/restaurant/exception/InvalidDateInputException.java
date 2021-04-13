package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateInputException extends RuntimeException {

    public static final String ERROR_CODE = "Id.NotMatch";

    public InvalidDateInputException(String message) {
        super(message);
    }
}
