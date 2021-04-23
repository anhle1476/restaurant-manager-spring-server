package com.codegym.restaurant.exception;

public class BillDetailNotFoundException extends RuntimeException{
    public static final String ERROR_CODE = "Bill.NotFound";

    public BillDetailNotFoundException(String message) {
        super(message);
    }

}
