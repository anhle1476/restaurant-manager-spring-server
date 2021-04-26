package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppTableNameExistsException extends RuntimeException{
    public static final String ERROR_CODE = "AppTable.NameExists";

    public AppTableNameExistsException(String message) {
        super(message);
    }

}
