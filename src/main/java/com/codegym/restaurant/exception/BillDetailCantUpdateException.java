package com.codegym.restaurant.exception;

public class BillDetailCantUpdateException extends RuntimeException{
    public static final String ERROR_CODE = "BillDetail.CantUpdate";

    public BillDetailCantUpdateException(String message) {
        super(message);
    }

}
