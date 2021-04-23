package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SalaryDetailNotFoundException extends RuntimeException{

    public static final String ERROR_CODE = "SalaryDetail.NotFound";

    public SalaryDetailNotFoundException(String message) {
        super(message);
    }
}
