package com.codegym.restaurant.exception;

public class BillDetailUpdateFailedException extends RuntimeException{
    public static final String ERROR_CODE = "BillDetail.UpdateFailed";

    public BillDetailUpdateFailedException(String message) {
        super(message);
    }

}
