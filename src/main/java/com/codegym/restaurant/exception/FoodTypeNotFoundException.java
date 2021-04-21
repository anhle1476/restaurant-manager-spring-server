package com.codegym.restaurant.exception;

public class FoodTypeNotFoundException extends RuntimeException{
    public static final String ERROR_CODE = "FoodType.NotFound";

    public FoodTypeNotFoundException(String message) {
        super(message);
    }

}
