package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AreaNameExistsException extends RuntimeException{
    public static final String ERROR_CODE = "Area.NameExists";

    public AreaNameExistsException(String message) {
        super(message);
    }

}
