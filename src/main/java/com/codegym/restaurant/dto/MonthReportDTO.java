package com.codegym.restaurant.dto;

import com.codegym.restaurant.model.bussiness.Food;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class MonthReportDTO {
    private Map<LocalDate,Long> incomeByDate;
    private Map<Food,Integer> foodQuantitySold;
    private long totalOfMonth;
}
