package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.hr.Shift;
import com.codegym.restaurant.service.impl.ShiftServiceImpl;
import com.codegym.restaurant.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ShiftServiceImpl shiftService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER','CHEF','MISC')")
    public ResponseEntity<List<Shift>> show(@RequestParam(name = "deleted", defaultValue = "false") String deleted) {
        List<Shift> shiftList = deleted.equals("both")
                ? shiftService.getAllWithBothDeletedStatus()
                : deleted.equals("true")
                ? shiftService.getAllDeleted()
                : shiftService.getAll();
        return new ResponseEntity<>(shiftList, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody Shift shift, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        return new ResponseEntity<>(shiftService.create(shift), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> update(@Valid @RequestBody Shift shift,
                                    BindingResult result,
                                    @PathVariable(value = "id") Integer id) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!shift.getId().equals(id))
            throw new IdNotMatchException("Id không trùng hợp");
        return new ResponseEntity<>(shiftService.update(shift), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
        shiftService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Shift> restore(@PathVariable(value = "id") Integer id) {
        shiftService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
