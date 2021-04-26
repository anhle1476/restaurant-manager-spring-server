package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.TableGroupingDTO;
import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.exception.InvalidDateInputException;
import com.codegym.restaurant.exception.AppTableNameExistsException;
import com.codegym.restaurant.model.bussiness.AppTable;
import com.codegym.restaurant.service.AppTableService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/tables")
public class AppTableController {
    @Autowired
    private AppTableService appTableService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    private ResponseEntity<List<AppTable>> listTable(@RequestParam(value = "deleted", required = false) String deleted) {
        List<AppTable> appTableList = deleted == null || deleted.equals("true")
                ? appTableService.getAll()
                : appTableService.getAllDeleted();
        return new ResponseEntity<>(appTableList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<AppTable> getTableById(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(appTableService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> createAppTable(@Valid @RequestBody AppTable appTable, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);

        try {
            return new ResponseEntity<>(appTableService.create(appTable), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new AppTableNameExistsException("Tên bàn này đã tồn tại");
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateAppTable(
            @Valid @RequestBody AppTable appTable,
            BindingResult result,
            @PathVariable(value = "id") Integer id
    ) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);

        if (!appTable.getId().equals(id))
            throw new InvalidDateInputException("Id không trùng khớp");

        try {
            return new ResponseEntity<>(appTableService.update(appTable), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new AppTableNameExistsException("Tên bàn này đã tồn tại");
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deletedAppTable(@PathVariable(value = "id") Integer id) {
        appTableService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}")
    private ResponseEntity<?> restoreAppTable(@PathVariable(value = "id") Integer id) {
        appTableService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/grouping")
    private ResponseEntity<?> groupingTable(@Valid @RequestBody TableGroupingDTO tableGroupingDTO, BindingResult result,
                                            @PathVariable(value = "id") Integer id) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);

        if (!tableGroupingDTO.getParent().equals(id))
            throw new IdNotMatchException("Id bàn chính không khớp");

        return new ResponseEntity<>(appTableService.groupingTables(tableGroupingDTO), HttpStatus.OK);
    }


    @PostMapping("/{id}/separate")
    private ResponseEntity<?> separateTable(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(appTableService.separateTables(id), HttpStatus.OK);
    }
}
