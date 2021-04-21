package com.codegym.restaurant.exception;

public class FoodTypeNameExistsException extends RuntimeException{
    public static final String ERROR_CODE = "FoodType.NameExists";

    public FoodTypeNameExistsException(String message) {
        super(message);
    }

}
