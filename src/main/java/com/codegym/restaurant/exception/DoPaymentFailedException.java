package com.codegym.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DoPaymentFailedException extends RuntimeException {

    public static final String ERROR_CODE = "doPayment.Failed";

    public DoPaymentFailedException(String message) {
        super(message);
    }
}
