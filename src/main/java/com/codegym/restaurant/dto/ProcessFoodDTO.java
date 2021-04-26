package com.codegym.restaurant.dto;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;

@Data
public class ProcessFoodDTO {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String billId;

    private Integer foodId;

    private Integer processQuantity;
}
