package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.bussiness.FoodType;
import com.codegym.restaurant.service.FoodService;
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
@RequestMapping("/api/v1/foods")
public class FoodController {
    @Autowired
    private FoodService foodService;
    @Autowired
    private AppUtils appUtils;
    @GetMapping
    public ResponseEntity<List<Food>> show(@RequestParam(name = "deleted", required = false) String deleted) {
        List<Food> foodTypes = deleted == null || !deleted.equals("true")
                ? foodService.getAll()
                : foodService.getAllDeleted();
        return new ResponseEntity<>(foodTypes, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createFoodType(@Valid @RequestBody Food food, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        return new ResponseEntity<>(foodService.create(food), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable(value = "id") Integer id,
            @Valid @RequestBody Food food,
            BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!food.getId().equals(id))
            throw new IdNotMatchException("Id không trùng khớp");
        return new ResponseEntity<>(foodService.update(food), HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable(value = "id") Integer id) {
        foodService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/{id}/restore")
    public ResponseEntity<FoodType> restore(@PathVariable(value = "id") Integer id) {
        foodService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
