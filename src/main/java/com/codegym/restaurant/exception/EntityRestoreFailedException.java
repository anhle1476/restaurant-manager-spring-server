package com.codegym.restaurant.exception;


public class EntityRestoreFailedException extends RuntimeException {


    public static final String ERROR_CODE = "Entity.RestoreFailed";

    public EntityRestoreFailedException(String message) {
        super(message);
    }
}
