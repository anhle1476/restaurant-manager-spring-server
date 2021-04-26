package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppTableNotAParentException extends RuntimeException{
    public static final String ERROR_CODE = "AppTable.NotAParent";

    public AppTableNotAParentException(String message) {
        super(message);
    }

}
