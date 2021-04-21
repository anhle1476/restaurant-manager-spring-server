package com.codegym.restaurant.exception;

public class FoodNameExistsException extends RuntimeException{
    public static final String ERROR_CODE = "Food.NameExists";

    public FoodNameExistsException(String message) {
        super(message);
    }

}
