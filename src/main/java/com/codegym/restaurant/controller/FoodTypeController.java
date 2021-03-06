package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.FoodTypeNameExistsException;
import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.bussiness.FoodType;
import com.codegym.restaurant.service.FoodTypeService;
import com.codegym.restaurant.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/api/v1/food-types")
public class FoodTypeController {
    @Autowired
    private FoodTypeService foodTypeService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER','CHEF','MISC')")
    public ResponseEntity<List<FoodType>> show(@RequestParam(name = "deleted", required = false) String deleted) {
        List<FoodType> foodTypes = deleted == null || !deleted.equals("true")
                ? foodTypeService.getAll()
                : foodTypeService.getAllDeleted();
        return new ResponseEntity<>(foodTypes, HttpStatus.OK);
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createFoodType(@Valid @RequestBody FoodType foodType, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);

        try {
            return new ResponseEntity<>(foodTypeService.create(foodType), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new FoodTypeNameExistsException("T??n lo???i m??n ???? t???n t???i");
        }
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateFoodType(
            @PathVariable(value = "id") Integer id,
            @Valid @RequestBody FoodType foodType,
            BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!foodType.getId().equals(id))
            throw new IdNotMatchException("Id kh??ng tr??ng kh???p");

        try {
            return new ResponseEntity<>(foodTypeService.update(foodType), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new FoodTypeNameExistsException("T??n lo???i m??n ???? t???n t???i");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteFoodType(@PathVariable(value = "id") Integer id) {
        foodTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<FoodType> restore(@PathVariable(value = "id") Integer id) {
        foodTypeService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{id}/foods")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Food>> findAllFoodByFoodType(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(foodTypeService.findAllFoodByFoodType(id), HttpStatus.OK);
    }
}
