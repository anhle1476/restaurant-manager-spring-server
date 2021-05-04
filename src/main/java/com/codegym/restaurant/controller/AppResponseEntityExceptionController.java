package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.ExceptionResponseDTO;

import com.codegym.restaurant.exception.AppTableNotAParentException;
import com.codegym.restaurant.exception.AreaNameExistsException;
import com.codegym.restaurant.exception.BillDetailUpdateFailedException;
import com.codegym.restaurant.exception.BillNotFoundException;
import com.codegym.restaurant.exception.BillInUsingTableException;
import com.codegym.restaurant.exception.BillUpdateFailedException;
import com.codegym.restaurant.exception.DoPaymentFailedException;
import com.codegym.restaurant.exception.AppTableNotFoundException;
import com.codegym.restaurant.exception.AreaNotFoundException;
import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.FoodImageUploadFailedException;
import com.codegym.restaurant.exception.FoodNameExistsException;
import com.codegym.restaurant.exception.FoodNotFoundException;
import com.codegym.restaurant.exception.FoodTypeDeleteFailedException;
import com.codegym.restaurant.exception.FoodTypeNameExistsException;
import com.codegym.restaurant.exception.FoodTypeNotFoundException;
import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.exception.InvalidDateInputException;
import com.codegym.restaurant.exception.InvalidScheduleException;
import com.codegym.restaurant.exception.ReservingOrderNotFoundException;
import com.codegym.restaurant.exception.RoleNotFoundException;
import com.codegym.restaurant.exception.SalaryDetailNotFoundException;
import com.codegym.restaurant.exception.ScheduleNotFoundException;
import com.codegym.restaurant.exception.ShiftNotFoundException;
import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.exception.AppTableNameExistsException;
import com.codegym.restaurant.exception.UpdatePasswordFailedException;
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

    @ExceptionHandler(value = UpdatePasswordFailedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleUpdatePasswordFailedException(UpdatePasswordFailedException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), UpdatePasswordFailedException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<ExceptionResponseDTO> handleInvalidDateInputException(InvalidDateInputException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), InvalidDateInputException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ScheduleNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleScheduleNotFoundException(ScheduleNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), ScheduleNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);// không tìm thấy
    }

    @ExceptionHandler(value = InvalidScheduleException.class)
    public ResponseEntity<ExceptionResponseDTO> handleInvalidScheduleException(InvalidScheduleException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), InvalidScheduleException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //gởi dữ liệu lên bi lỗi (client)
    }

    @ExceptionHandler(value = ReservingOrderNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleReservingOrderNotFoundException(ReservingOrderNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), ReservingOrderNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //
    }

    @ExceptionHandler(value = BillNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleBillNotFoundException(BillNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), BillNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BillUpdateFailedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDeleteBillFailException(BillUpdateFailedException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), BillUpdateFailedException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DoPaymentFailedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDoPaymentFailException(DoPaymentFailedException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), DoPaymentFailedException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AppTableNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAppTableNotFoundException(AppTableNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), AppTableNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AppTableNameExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAppTableNameExistsException(AppTableNameExistsException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), AppTableNameExistsException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AppTableNotAParentException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAppTableNotAParentException(AppTableNotAParentException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), AppTableNotAParentException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FoodImageUploadFailedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleFoodImageUploadFailedException(FoodImageUploadFailedException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), FoodImageUploadFailedException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = FoodNameExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleFoodNameExistsException(FoodNameExistsException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), FoodNameExistsException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = FoodNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleFoodNotFoundException(FoodNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), FoodNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = FoodTypeDeleteFailedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleFoodTypeDeleteFailedException(FoodTypeDeleteFailedException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), FoodTypeDeleteFailedException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FoodTypeNameExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleFoodTypeNameExistsException(FoodTypeNameExistsException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), FoodTypeNameExistsException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FoodTypeNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleFoodTypeNotFoundException(FoodTypeNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), FoodTypeNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = SalaryDetailNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleSalaryDetailNotFoundException(SalaryDetailNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), SalaryDetailNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AreaNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAreaNotFoundException(AreaNotFoundException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), AreaNotFoundException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AreaNameExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAreaNameExistsException(AreaNameExistsException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), AreaNameExistsException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BillDetailUpdateFailedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleBillDetailUpdateFailedException(BillDetailUpdateFailedException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), BillDetailUpdateFailedException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BillInUsingTableException.class)
    public ResponseEntity<ExceptionResponseDTO> handleBillInUsingTableException(BillInUsingTableException ex) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(ex.getMessage(), BillInUsingTableException.ERROR_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
