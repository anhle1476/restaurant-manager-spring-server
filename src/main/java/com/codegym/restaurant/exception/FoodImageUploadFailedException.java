package com.codegym.restaurant.exception;

public class FoodImageUploadFailedException extends RuntimeException {
    public static final String ERROR_CODE = "Food.ImageUploadFailed";

    public FoodImageUploadFailedException(String message) {
        super(message);
    }
}
