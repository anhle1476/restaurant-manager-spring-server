package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.ExceptionResponseDTO;

import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.exception.InvalidDateInputException;
import com.codegym.restaurant.exception.RoleNotFoundException;
import com.codegym.restaurant.exception.ScheduleNotFoundException;
import com.codegym.restaurant.exception.ShiftNotFoundException;
import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.exception.ViolationNotFoundException;
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
    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleRoleNotFoundException(RoleNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), RoleNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = InvalidDateInputException.class)
    public ResponseEntity<ExceptionResponseDTO> InvalidDateInputException(InvalidDateInputException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), InvalidDateInputException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = ScheduleNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> ScheduleNotFoundException(ScheduleNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), ScheduleNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
