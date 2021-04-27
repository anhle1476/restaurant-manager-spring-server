package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.AreaNameExistsException;
import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.bussiness.Area;
import com.codegym.restaurant.service.AreaService;
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
@RequestMapping("/api/v1/areas")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    private ResponseEntity<List<Area>> listArea(@RequestParam(value = "deleted", required = false) String deleted) {
        List<Area> list = deleted == null || !deleted.equals("true")
                ? areaService.getAll()
                : areaService.getAllDeleted();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getAreaById(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(areaService.getById(id),HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> createArea(@Valid @RequestBody Area area, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        try {
            return new ResponseEntity<>(areaService.create(area), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new AreaNameExistsException("Tên khu vực này đã tồn tại");
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateArea(@Valid @RequestBody Area area, BindingResult result,
                                         @PathVariable(value = "id") Integer id) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!area.getId().equals(id))
            throw new IdNotMatchException("Id không trùng khớp");
        try {
            return new ResponseEntity<>(areaService.update(area), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new AreaNameExistsException("Tên khu vực này đã tồn tại");
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deletedArea(@PathVariable(value = "id") Integer id) {
        areaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    private ResponseEntity<?> restoreArea(@PathVariable(value = "id") Integer id) {
        areaService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
