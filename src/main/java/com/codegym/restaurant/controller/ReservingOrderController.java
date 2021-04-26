package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.bussiness.ReservingOrder;
import com.codegym.restaurant.service.impl.ReservingOrderServiceImpl;
import com.codegym.restaurant.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/reserving-orders")
public class ReservingOrderController {
    @Autowired
    private ReservingOrderServiceImpl reservingOrderService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    private ResponseEntity<List<ReservingOrder>> listReservingOrder(
            @RequestParam(value = "deleted", required = false) String deleted,
            @RequestParam(value = "date", required = false) LocalDate date) {
        List<ReservingOrder> reservingOrderList;
        if (date != null) {
            return new ResponseEntity<>(reservingOrderService.findReservingOrdersBy(date), HttpStatus.OK);
        }
        reservingOrderList = deleted == null || !deleted.equals("true")
                ? reservingOrderService.getAll()
                : reservingOrderService.getAllDeleted();
        return new ResponseEntity<>(reservingOrderList, HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> createReservingOrder(@Valid @RequestBody ReservingOrder reservingOrder, BindingResult result) {
        if (result.hasErrors()) {
            return appUtils.mapErrorToResponse(result);
        }
        return new ResponseEntity<>(reservingOrderService.create(reservingOrder), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateReservingOrder(@Valid @RequestBody ReservingOrder reservingOrder, BindingResult result,
                                                   @PathVariable(value = "id") Integer id) {
        if (result.hasErrors()) {
            return appUtils.mapErrorToResponse(result);
        }
        if (!reservingOrder.getId().equals(id)) {
            throw new IdNotMatchException("Id không trùng hợp");
        }
        return new ResponseEntity<>(reservingOrderService.update(reservingOrder), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteReservingOrder(@PathVariable(value = "id") Integer id) {
        reservingOrderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    private ResponseEntity<?> restoreReservingOrder(@PathVariable(value = "id") Integer id) {
        reservingOrderService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
