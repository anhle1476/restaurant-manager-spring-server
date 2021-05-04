package com.codegym.restaurant.exception;

public class BillNotFoundException extends RuntimeException{
    public static final String ERROR_CODE = "Bill.NotFound";

    public BillNotFoundException(String message) {
        super(message);
    }

}
