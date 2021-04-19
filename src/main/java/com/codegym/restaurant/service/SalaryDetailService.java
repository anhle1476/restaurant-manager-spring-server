package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.SalaryDifferenceDTO;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.model.hr.Violation;

import java.time.LocalDate;
import java.util.Map;

public interface SalaryDetailService {

    void updateSalaryWhenScheduleChanged(LocalDate firstDateOfMonth, Map<Integer, SalaryDifferenceDTO> differenceMap);

    void updateSalaryDetailsWhenStaffSalaryChanged(Staff staff);

    void updateSalaryDetailsWhenViolationFinesPercentChanged(Violation violation);
}
