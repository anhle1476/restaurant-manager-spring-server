package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.hr.Violation;
import com.codegym.restaurant.service.impl.ViolationServiceImpl;
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
@RequestMapping("/api/v1/violations")
public class ViolationController {
    @Autowired
    private ViolationServiceImpl violationService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER','CHEF','MISC')")
    public ResponseEntity<List<Violation>> show(@RequestParam(value = "deleted", required = false) String deleted){
        List<Violation> violations = deleted == null || !deleted.equals("true")
                ? violationService.getAll()
                : violationService.getAllDeleted();
        return new ResponseEntity<>(violations, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody Violation violation, BindingResult result){
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        return new ResponseEntity<>(violationService.create(violation), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> update(@Valid @RequestBody Violation violation,
                                    BindingResult result,
                                    @PathVariable(value = "id") Integer id){
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!violation.getId().equals(id))
            throw new IdNotMatchException("Id không trùng hợp");
        return new ResponseEntity<>(violationService.update(violation), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Violation> delete(@PathVariable(value = "id") Integer id){
        violationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Violation> restore(@PathVariable(value = "id") Integer id){
        violationService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
