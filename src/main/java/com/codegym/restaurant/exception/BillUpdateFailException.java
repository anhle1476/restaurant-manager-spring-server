package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BillUpdateFailException extends RuntimeException {

    public static final String ERROR_CODE = "DeleteBill.Failed";

    public BillUpdateFailException(String message) {
        super(message);
    }
}
