package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.bussiness.FoodType;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.service.FoodTypeService;
import com.codegym.restaurant.utils.AppUtils;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/api/v1/food-types")
public class FoodTypeController {
    @Autowired
    private FoodTypeService foodTypeService;

    @Autowired
    private AppUtils appUtils;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<FoodType>> show(@RequestParam(name = "deleted", required = false) String deleted) {
        List<FoodType> foodTypes = deleted == null || !deleted.equals("true")
                ? foodTypeService.getAll()
                : foodTypeService.getAllDeleted();
        return new ResponseEntity<>(foodTypes, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createFoodType(@Valid @RequestBody FoodType foodType, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        FoodType foodType1 = modelMapper.map(foodType, FoodType.class);
        return new ResponseEntity<>(foodTypeService.create(foodType1), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable(value = "id") Integer id,
            @Valid @RequestBody FoodType foodType,
            BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!foodType.getId().equals(id))
            throw new IdNotMatchException("Id không trùng khớp");
        return new ResponseEntity<>(foodTypeService.update(foodType), HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable(value = "id") Integer id) {
        foodTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/{id}/restore")
    public ResponseEntity<FoodType> restore(@PathVariable(value = "id") Integer id) {
        foodTypeService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{id}/foods")
    public ResponseEntity<List<Food>> AllStaffsOfRoleId(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(foodTypeService.findAllFoodByFoodType(id), HttpStatus.OK);
    }
}
