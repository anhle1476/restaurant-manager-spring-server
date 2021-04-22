package com.codegym.restaurant.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class FoodFormDTO {
    private Integer id;

    @Column(name = "food_name", unique = true)
    @Pattern(regexp = "^[\\pL 0-9()_:-]{2,50}$", message = "Tên món ăn phải chứa từ 2-50 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Tên món không được trống")
    private String name;

    @Min(value = 0,message = "Giá không được âm")
    private long price;

    @Pattern(regexp = "^[\\pL /0-9()_:-]{1,50}$", message = "Tên đơn vị từ 1-50 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Đơn vị không được trống")
    private String unit;

    @ManyToOne
    @NotNull(message = "Loại món không được trống")
    private Integer foodTypeId;

    private MultipartFile image;
}
