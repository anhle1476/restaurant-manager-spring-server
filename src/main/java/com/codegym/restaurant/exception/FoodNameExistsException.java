package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FoodNameExistsException extends RuntimeException{
    public static final String ERROR_CODE = "Food.NameExists";

    public FoodNameExistsException(String message) {
        super(message);
    }

}
