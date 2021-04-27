package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.hr.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FoodRepository extends JpaSoftDeleteRepository<Food,Integer> {
    @Query("SELECT f FROM Food f " +
            "WHERE f.deleted = FALSE " +
            "AND f.foodType.deleted = FALSE " +
            "AND f.foodType.id = :id")
    List<Food> findFoodByFoodTypeId(Integer id);
}
