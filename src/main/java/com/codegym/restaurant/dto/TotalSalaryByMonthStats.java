package com.codegym.restaurant.dto;

import java.time.LocalDate;

public interface TotalSalaryByMonthStats {
    LocalDate getFirstDateOfMonth();
    Long getTotalSalary();
    Integer getTotalStaff();
}
