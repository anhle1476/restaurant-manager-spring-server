package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.bussiness.FoodType;

import java.util.List;

public interface FoodTypeRepository extends JpaSoftDeleteRepository<FoodType,Integer> {

}
