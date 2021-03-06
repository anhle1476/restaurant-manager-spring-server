package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.FoodFormDTO;
import com.codegym.restaurant.exception.FoodNameExistsException;
import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.service.FoodService;
import com.codegym.restaurant.utils.AppUtils;
import com.codegym.restaurant.validator.FoodFormImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/foods")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private FoodFormImageValidator imageValidator;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER','CHEF','MISC')")
    public ResponseEntity<List<Food>> showAllFoods(@RequestParam(name = "deleted", required = false) String deleted) {
        List<Food> foodTypes = deleted == null || !deleted.equals("true")
                ? foodService.getAll()
                : foodService.getAllDeleted();
        return new ResponseEntity<>(foodTypes, HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createFood(@Valid @ModelAttribute FoodFormDTO food, BindingResult result) {
        imageValidator.validate(food, result);
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        try {
            return new ResponseEntity<>(foodService.create(food), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new FoodNameExistsException("T??n m??n ??n ???? t???n t???i");
        }
    }

    @PutMapping(
            path = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateFood(
            @PathVariable(value = "id") Integer id,
            @Valid @ModelAttribute FoodFormDTO food,
            BindingResult result) {
        imageValidator.validate(food, result);
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!food.getId().equals(id))
            throw new IdNotMatchException("Id kh??ng tr??ng kh???p");
        try {
            return new ResponseEntity<>(foodService.update(food), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new FoodNameExistsException("T??n m??n ??n ???? t???n t???i");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteFood(@PathVariable(value = "id") Integer id) {
        foodService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Food> restoreFood(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(foodService.restore(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/toggle-availability")
    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF')")
    public ResponseEntity<Food> toggleAvailability(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(foodService.toggleAvailability(id), HttpStatus.OK);
    }
}
