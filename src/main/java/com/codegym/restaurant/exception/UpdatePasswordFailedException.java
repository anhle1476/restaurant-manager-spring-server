package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UpdatePasswordFailedException extends RuntimeException {

    public static final String ERROR_CODE = "Account.UpdatePasswordFailed";

    public UpdatePasswordFailedException(String message) {
        super(message);
    }
}
