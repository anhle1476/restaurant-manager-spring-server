package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FoodTypeDeleteFailedException extends RuntimeException {
    public static final String ERROR_CODE = "FoodType.DeleteFailed";
    public FoodTypeDeleteFailedException(String message) {
        super(message);
    }
}
