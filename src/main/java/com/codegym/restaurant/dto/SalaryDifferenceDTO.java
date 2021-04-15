package com.codegym.restaurant.dto;

import com.codegym.restaurant.model.hr.Violation;
import lombok.Data;

@Data
public class SalaryDifferenceDTO {
    private int numberOfShift;

    private float overtimeHours;

    private Violation oldViolation;

    private Violation newViolation;
}
