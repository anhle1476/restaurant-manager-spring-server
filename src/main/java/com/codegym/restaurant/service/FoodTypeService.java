package com.codegym.restaurant.service;

import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.bussiness.FoodType;

import java.util.List;

public interface FoodTypeService extends BaseService<FoodType,Integer> {
    List<Food> findAllFoodByFoodType(Integer id);
}
