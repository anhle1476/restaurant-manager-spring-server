package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.InvalidDateInputException;
import com.codegym.restaurant.model.bussiness.AppTable;
import com.codegym.restaurant.service.impl.AppTableServiceImpl;
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
import java.util.List;

@RestController
@RequestMapping("api/v1/tables")
public class AppTableController {
    @Autowired
    private AppTableServiceImpl appTableService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    private ResponseEntity<List<AppTable>> listTable(@RequestParam(value = "deleted", required = false) String deleted) {
        List<AppTable> appTableList = deleted == null || deleted.equals(true)
                ? appTableService.getAll()
                : appTableService.getAllDeleted();
        return new ResponseEntity<>(appTableList, HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> createAppTable(@Valid @RequestBody AppTable appTable, BindingResult result) {
        if (result.hasErrors()) {
            return appUtils.mapErrorToResponse(result);
        }
        return new ResponseEntity<>(appTableService.create(appTable), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateAppTable(@Valid @RequestBody AppTable appTable,
                                             BindingResult result, @PathVariable(value = "id") Integer id) {
        if (result.hasErrors()) {
            return appUtils.mapErrorToResponse(result);
        }
        if (!appTable.getId().equals(id)) {
            throw new InvalidDateInputException("Id không trùng khớp");
        }
        return new ResponseEntity<>(appTableService.update(appTable), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deletedAppTable(@PathVariable(value = "id") Integer id) {
        appTableService.delete(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}")
    private ResponseEntity<?> restoreAppTable(@PathVariable(value = "id") Integer id) {
        appTableService.restore(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }
}
