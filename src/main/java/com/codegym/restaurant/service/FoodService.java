package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.FoodFormDTO;
import com.codegym.restaurant.model.bussiness.Food;

import java.util.List;


public interface FoodService {
    List<Food> getAll();

    List<Food> getAllDeleted();

    Food getById(Integer id);

    Food create(FoodFormDTO g);

    Food update(FoodFormDTO g);

    void delete(Integer id);

    void restore(Integer id);
}
