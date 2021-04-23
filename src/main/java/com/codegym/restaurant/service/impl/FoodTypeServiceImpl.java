package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.FoodTypeDeleteFailedException;
import com.codegym.restaurant.exception.FoodTypeException;
import com.codegym.restaurant.exception.ShiftNotFoundException;
import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.bussiness.FoodType;
import com.codegym.restaurant.model.hr.Shift;
import com.codegym.restaurant.repository.FoodRepository;
import com.codegym.restaurant.repository.FoodTypeRepository;
import com.codegym.restaurant.service.FoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FoodTypeServiceImpl implements FoodTypeService {
    @Autowired
    private FoodTypeRepository foodTypeRepository;
    @Autowired
    private FoodRepository foodRepository;

    @Override
    public List<FoodType> getAll() {
        return foodTypeRepository.findAllAvailable();
    }

    @Override
    public List<FoodType> getAllDeleted() {
        return foodTypeRepository.findAllDeleted();
    }

    @Override
    public FoodType getById(Integer id) {
        return foodTypeRepository.findAvailableById(id)
                .orElseThrow(() -> new FoodTypeException("Loại món không tồn tại"));
    }

    @Override
    public FoodType create(FoodType foodType) {
        return foodTypeRepository.save(foodType);
    }

    @Override
    public FoodType update(FoodType foodType) {
        return foodTypeRepository.save(foodType);
    }

    @Override
    public void delete(Integer id) {
        FoodType foodType = getById(id);
        List<Food> foods = foodRepository.findFoodByFoodTypeId(id);
        if (!foods.isEmpty() )
            throw new FoodTypeDeleteFailedException("Trong menu vẫn còn món của loại này không xóa được  ");
        foodType.setDeleted(true);
        foodTypeRepository.save(foodType);
    }

    @Override
    public void restore(Integer integer) {
        FoodType foodType = foodTypeRepository.findById(integer)
                .orElseThrow(() -> new FoodTypeException("Loại món này không tồn tại"));
        if (!foodType.isDeleted())
            throw new EntityRestoreFailedException("Không phục hồi khi đối tượng chưa xóa");
        foodType.setDeleted(false);
        foodTypeRepository.save(foodType);
    }

    @Override
    public List<Food> findAllFoodByFoodType(Integer id) {
        return foodRepository.findFoodByFoodTypeId(id);
    }
}
