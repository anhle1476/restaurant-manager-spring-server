package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.ExceptionResponseDTO;
import com.codegym.restaurant.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class AppResponseEntityExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = EntityRestoreFailedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleEntityRestoreFailedException(EntityRestoreFailedException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), EntityRestoreFailedException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = StaffNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleStaffNotFoundException(StaffNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), StaffNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ShiftNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleShiftNotFoundException(ShiftNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), ShiftNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ViolationNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleViolationNotFoundException(ViolationNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), ViolationNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IdNotMatchException.class)
    public ResponseEntity<ExceptionResponseDTO> handleIdNotMatchException(IdNotMatchException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), IdNotMatchException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
