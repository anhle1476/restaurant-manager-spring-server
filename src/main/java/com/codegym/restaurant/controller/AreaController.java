package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.bussiness.Area;
import com.codegym.restaurant.service.impl.AreaServiceImpl;
import com.codegym.restaurant.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/areas")
public class AreaController {

    @Autowired
    private AreaServiceImpl areaService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    private ResponseEntity<List<Area>> listArea(@RequestParam(value = "deleted", required = false) String deleted) {
        List<Area> list = deleted == null || !deleted.equals("true")
                ? areaService.getAll()
                : areaService.getAllDeleted();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> createArea(@Valid @RequestBody Area area, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        return new ResponseEntity<>(areaService.create(area), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateArea(@Valid @RequestBody Area area, BindingResult result,
                                         @PathVariable(value = "id") Integer id) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!area.getId().equals(id))
            throw new IdNotMatchException("Id không trùng khớp");
        return new ResponseEntity<>(areaService.update(area), HttpStatus.CREATED);
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
