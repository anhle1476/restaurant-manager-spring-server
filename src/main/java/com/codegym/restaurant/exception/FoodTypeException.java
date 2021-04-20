package com.codegym.restaurant.exception;

public class FoodTypeException extends RuntimeException{
    public static final String ERROR_CODE = "FoodType.NotFound";

    public FoodTypeException(String message) {
        super(message);
    }

}
