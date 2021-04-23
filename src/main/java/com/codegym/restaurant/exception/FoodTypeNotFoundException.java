package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FoodTypeNotFoundException extends RuntimeException{
    public static final String ERROR_CODE = "FoodType.NotFound";

    public FoodTypeNotFoundException(String message) {
        super(message);
    }

}
