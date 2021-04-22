package com.codegym.restaurant.utils;

import com.cloudinary.utils.ObjectUtils;
import com.codegym.restaurant.exception.FoodImageUploadFailedException;
import com.codegym.restaurant.model.bussiness.Food;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UploadUtils {
    private static final String UPLOAD_FOLDER = "food_images";

    public Map buildUploadParams(Food food) {
        if (food == null || food.getId() == null)
            throw new FoodImageUploadFailedException("Không thể upload hình ảnh của món chưa được lưu");
        String publicId = String.format("%s/food_%d", UPLOAD_FOLDER, food.getId());
        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }
}
