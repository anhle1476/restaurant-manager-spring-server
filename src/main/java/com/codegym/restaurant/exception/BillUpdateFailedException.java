package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BillUpdateFailedException extends RuntimeException {

    public static final String ERROR_CODE = "Bill.UpdateFailed";

    public BillUpdateFailedException(String message) {
        super(message);
    }
}
