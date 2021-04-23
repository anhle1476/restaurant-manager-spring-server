package com.codegym.restaurant.dto;

import com.codegym.restaurant.model.bussiness.Food;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UpdateBillDetailDTO {
    @NotNull(message = "Món ăn không được trống")
    private Food food;
    @NotNull(message = "Số lượng món không được trống")
    private int quantity;
    @Min(value = 0,message = "Done Quantity không được âm")
    private int doneQuantity;
    @NotNull(message = "Thời gian order không được trống")
    private LocalDateTime lastOrderTime;

}
