package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.SalaryDifferenceDTO;

import java.time.LocalDate;
import java.util.Map;

public interface SalaryDetailService {

    void updateSalaryWhenScheduleChange(LocalDate firstDateOfMonth, Map<Integer, SalaryDifferenceDTO> differenceMap);


}
