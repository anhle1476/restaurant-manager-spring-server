package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AppTableNotFoundException extends RuntimeException{

    public static final String ERROR_CODE = "AppTable.NotFound";

    public AppTableNotFoundException(String message) {
        super(message);
    }
}
