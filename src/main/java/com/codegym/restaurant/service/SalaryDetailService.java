package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.SalaryDifferenceDTO;

import java.time.LocalDate;
import java.util.Map;

public interface SalaryDetailService {

    void salaryDetailsWithStaff(LocalDate firstDateOfMonth, Map<Integer, SalaryDifferenceDTO> differenceMap);

}
