package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidJwtException extends RuntimeException {
    public static final String ERROR_CODE = "Jwt.InvalidToken";

    public InvalidJwtException(String message) {
        super(message);
    }
}
