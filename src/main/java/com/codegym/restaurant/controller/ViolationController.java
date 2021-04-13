package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.hr.Violation;
import com.codegym.restaurant.service.impl.ViolationServiceImpl;
import com.codegym.restaurant.utils.AppUtils;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/violations")
public class ViolationController {
    @Autowired
    private ViolationServiceImpl violationService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    public ResponseEntity<List<Violation>> showViolation(@RequestParam(value = "deleted", required = false) String deleted){
        List<Violation> violations = deleted == null || !deleted.equals("true")
                ? violationService.getAll()
                : violationService.getAllDeleted();
        return new ResponseEntity<>(violations, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createViolation(@Valid @RequestBody Violation violation, BindingResult result){
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        return new ResponseEntity<>(violationService.create(violation), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateViolation(@Valid @RequestBody Violation violation,
                                    BindingResult result,
                                    @PathVariable(value = "id") Integer id){
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (violation.getId() != id)
            throw new IdNotMatchException("Id không trùng hợp");
        return new ResponseEntity<>(violationService.update(violation), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Violation> deleteViolation(@PathVariable(value = "id") Integer id){
        violationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Violation> restoreViolation(@PathVariable(value = "id") Integer id){
        violationService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
