package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AreaNotFoundException  extends RuntimeException{
    public static final String ERROR_CODE = "Area.NotFound";

    public AreaNotFoundException(String message){
        super(message);
    }
}
