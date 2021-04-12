package com.codegym.restaurant.controller;

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
    public ResponseEntity<List<Violation>> show(@RequestParam(value = "deleted", required = false) String deleted) {
        List<Violation> violations = deleted == null || !deleted.equals("true")
                ? violationService.getAll()
                : violationService.getAllDeleted();
        return new ResponseEntity<List<Violation>>(violations, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createViolation(@Valid @RequestBody Violation violation, BindingResult result) {
        if (result.hasErrors()) {
            return appUtils.mapErrorToResponse(result);
        }
        Violation saved = violationService.create(violation);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable(value = "id") Integer id, @RequestBody Violation violation, BindingResult result) {
        if (result.hasErrors()) {
            return appUtils.mapErrorToResponse(result);
        }
        Violation currentViolation = violationService.getById(id);
        if (currentViolation == null) {
            return new ResponseEntity<Violation>(HttpStatus.NOT_FOUND);
        }
        currentViolation.setName(violation.getName());
        currentViolation.setFinesPercent(violation.getFinesPercent());
        violationService.update(currentViolation);
        return new ResponseEntity<>(currentViolation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Violation> delete(@PathVariable(value = "id") Integer id) {
       violationService.delete(id);
       return new ResponseEntity<Violation>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Violation> undo(@PathVariable(value = "id")Integer id){
        violationService.restore(id);
        return new ResponseEntity<Violation>(HttpStatus.NO_CONTENT);
    }
}
