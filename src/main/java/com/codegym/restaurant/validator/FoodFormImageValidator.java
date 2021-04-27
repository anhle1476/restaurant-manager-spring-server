package com.codegym.restaurant.validator;


import com.codegym.restaurant.dto.FoodFormDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.entity.ContentType.*;

@Component
public class FoodFormImageValidator implements Validator {
    public static final List<String> CONTENT_TYPE_CHECKLIST = Arrays.asList(
            IMAGE_JPEG.getMimeType(),
            IMAGE_PNG.getMimeType(),
            IMAGE_GIF.getMimeType(),
            IMAGE_BMP.getMimeType(),
            IMAGE_SVG.getMimeType(),
            IMAGE_WEBP.getMimeType());
    @Override
    public boolean supports(Class<?> clazz) {
        return FoodFormDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FoodFormDTO dto = (FoodFormDTO) target;
        MultipartFile image = dto.getImage();

        boolean isNewFood = dto.getId() == null;
        boolean isImageEmpty = image == null || image.isEmpty();

        // case 1: cap nhat thuc an, khong cap nhat hinh anh -> OK
        if (!isNewFood && isImageEmpty)
            return;

        // case 2: tao thuc an, khong co hinh anh -> FAILED
        if (isNewFood && isImageEmpty) {
            errors.rejectValue("image",null, "Hình ảnh không được để trống");
            return;
        }

        // case 2: co file nhung khong dung dinh dang hinh anh -> FAILED
        if (!CONTENT_TYPE_CHECKLIST.contains(image.getContentType())) {
            errors.rejectValue("image",null, "Định dạng hình ảnh không hỗ trợ");
        }
    }
}
