package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.ExceptionResponseDTO;
import com.codegym.restaurant.exception.StaffNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class AppResponseEntityExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = StaffNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleStaffNotFoundException(StaffNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), StaffNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
