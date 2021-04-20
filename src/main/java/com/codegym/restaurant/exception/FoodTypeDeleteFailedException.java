package com.codegym.restaurant.exception;

public class FoodTypeDeleteFailedException extends RuntimeException {
    public static final String ERROR_CODE = "FoodType.DeleteFailed";
    public FoodTypeDeleteFailedException(String message) {
        super(message);
    }
}
