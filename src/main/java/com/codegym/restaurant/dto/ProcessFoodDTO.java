package com.codegym.restaurant.dto;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;

@Data
public class ProcessFoodDTO {
    private String billId;

    private Integer foodId;

    private Integer processQuantity;
}
