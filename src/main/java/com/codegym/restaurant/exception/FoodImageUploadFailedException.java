package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FoodImageUploadFailedException extends RuntimeException {
    public static final String ERROR_CODE = "Food.ImageUploadFailed";

    public FoodImageUploadFailedException(String message) {
        super(message);
    }
}
