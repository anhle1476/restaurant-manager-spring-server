package com.codegym.restaurant.exception;

public class FoodNotFountException extends RuntimeException {
    public static final String ERROR_CODE = "Food.NotFound";

    public FoodNotFountException(String message) {
        super(message);
    }
}
