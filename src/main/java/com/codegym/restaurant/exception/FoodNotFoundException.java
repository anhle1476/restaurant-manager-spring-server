package com.codegym.restaurant.exception;

public class FoodNotFoundException extends RuntimeException {
    public static final String ERROR_CODE = "Food.NotFound";

    public FoodNotFoundException(String message) {
        super(message);
    }
}
