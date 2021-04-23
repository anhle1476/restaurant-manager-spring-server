package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.AuthInfoDTO;
import com.codegym.restaurant.exception.BillDetailNotFoundException;
import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.bussiness.Bill;
import com.codegym.restaurant.service.BillService;
import com.codegym.restaurant.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
public class BillController {
    @Autowired
    private BillService billService;

    @Autowired
    private AppUtils appUtils;
    @GetMapping("/id")
    public  ResponseEntity<?> fineByUUID(@PathVariable(value = "id") String id) {
    return new ResponseEntity<>(billService.getById(id),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Bill>> showBillPayTimeIsNull() {
        return new ResponseEntity<>(billService.getAllBillPayTimeIsNull(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createBIll(@Valid @RequestBody Bill bill, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        try {
            return new ResponseEntity<>(billService.create(bill), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new BillDetailNotFoundException("Một hóa đơn không thể có 2 món trùng nhau");
        }
    }

    @PostMapping("{id}/payment")
    public ResponseEntity<?> doPayment(@Valid @RequestBody Bill bill,
                                       BindingResult result,
                                       @PathVariable(value = "id") String id,
                                       Principal principal){
        AuthInfoDTO infoDTO = appUtils.extractUserInfoFromToken(principal);
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!bill.getId().equals(id))
            throw new IdNotMatchException("Id không trùng hợp");
        return new ResponseEntity<>(billService.doPayment(bill, infoDTO.getId()), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateBill(
            @Valid @RequestBody Bill bill,
            BindingResult result,
            @PathVariable(value = "id") String id) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!bill.getId().equals(id))
            throw new IdNotMatchException("Id không trùng hợp");
        try {
            return new ResponseEntity<>(billService.update(bill), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new BillDetailNotFoundException("Trong 1 thời gian không thể có 2 bill giống nhau");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        billService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}