package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.FoodNotFountException;
import com.codegym.restaurant.exception.FoodTypeException;
import com.codegym.restaurant.exception.ShiftNotFoundException;
import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.bussiness.FoodType;
import com.codegym.restaurant.repository.FoodRepository;
import com.codegym.restaurant.repository.FoodTypeRepository;
import com.codegym.restaurant.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodRepository foodRepository;
    @Override
    public List<Food> getAll() {
        return foodRepository.findAllAvailable();
    }

    @Override
    public List<Food> getAllDeleted() {
        return foodRepository.findAllDeleted();
    }

    @Override
    public Food getById(Integer id) {
        return foodRepository.findAvailableById(id)
                .orElseThrow(() -> new FoodNotFountException("Món không tồn tại"));
    }

    @Override
    public Food create(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public Food update(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public void delete(Integer id) {

        Food food = getById(id);
        food.setDeleted(true);
        foodRepository.save(food);
    }

    @Override
    public void restore(Integer integer) {
        Food food = foodRepository.findById(integer)
                .orElseThrow(() -> new ShiftNotFoundException("Loại món này không tồn tại"));
        if (!food.isDeleted())
            throw new EntityRestoreFailedException("Không phục hồi khi đối tượng chưa xóa");
        food.setDeleted(false);
        foodRepository.save(food);
    }
}
