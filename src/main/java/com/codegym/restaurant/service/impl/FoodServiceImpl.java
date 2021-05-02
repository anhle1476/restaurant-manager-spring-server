package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.FoodFormDTO;
import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.FoodImageUploadFailedException;
import com.codegym.restaurant.exception.FoodNotFoundException;
import com.codegym.restaurant.exception.FoodTypeNotFoundException;
import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.bussiness.FoodType;
import com.codegym.restaurant.repository.FoodRepository;
import com.codegym.restaurant.repository.FoodTypeRepository;
import com.codegym.restaurant.service.FoodService;
import com.codegym.restaurant.service.UploadService;
import com.codegym.restaurant.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private UploadUtils uploadUtils;

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
                .orElseThrow(() -> new FoodNotFoundException("Món không tồn tại hoặc đã bị xóa"));
    }

    @Override
    public Food create(FoodFormDTO foodDTO) {
        Food food = new Food();

        updateFoodBasicInfo(foodDTO, food);
        updateFoodType(foodDTO, food);
        foodRepository.save(food);
        uploadAndSaveFoodImage(foodDTO, food);

        return food;
    }

    @Override
    public Food update(FoodFormDTO foodDTO) {
        Food food = getById(foodDTO.getId());

        updateFoodBasicInfo(foodDTO, food);
        if (!food.getFoodType().getId().equals(foodDTO.getFoodTypeId()))
            updateFoodType(foodDTO, food);
        if (foodDTO.getImage() != null)
            uploadAndSaveFoodImage(foodDTO, food);

        return foodRepository.save(food);
    }

    private void updateFoodBasicInfo(FoodFormDTO foodDTO, Food food) {
        food.setName(foodDTO.getName());
        food.setPrice(foodDTO.getPrice());
        food.setUnit(foodDTO.getUnit());
        food.setAvailable(true);
    }

    private void updateFoodType(FoodFormDTO foodDTO, Food food) {
        FoodType foodType = foodTypeRepository.findAvailableById(foodDTO.getFoodTypeId())
                .orElseThrow(() -> new FoodTypeNotFoundException("Loại món không tồn tại"));
        food.setFoodType(foodType);
    }

    private void uploadAndSaveFoodImage(FoodFormDTO foodDTO, Food food) {
        try {
            Map uploadResult = uploadService.upload(foodDTO.getImage(), uploadUtils.buildUploadParams(food));
            String url = (String) uploadResult.get("secure_url");
            food.setImageUrl(url);
            foodRepository.save(food);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FoodImageUploadFailedException("Upload hình ảnh thất bại");
        }
    }

    @Override
    public void delete(Integer id) {
        Food food = getById(id);
        food.setDeleted(true);
        foodRepository.save(food);
    }

    @Override
    public Food restore(Integer id) {
        Food food = getById(id);
        if (!food.isDeleted())
            throw new EntityRestoreFailedException("Không thể phục hồi khi đối tượng chưa xóa");
        food.setDeleted(false);
        return foodRepository.save(food);
    }

    @Override
    public Food toggleAvailability(Integer id) {
        Food food = getById(id);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
