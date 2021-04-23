package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.SalaryDifferenceDTO;
import com.codegym.restaurant.dto.TotalSalaryByMonthStats;
import com.codegym.restaurant.model.hr.SalaryDetail;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.model.hr.Violation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SalaryDetailService {

    void updateSalaryWhenScheduleChanged(LocalDate firstDateOfMonth, Map<Integer, SalaryDifferenceDTO> differenceMap);

    void updateSalaryDetailsWhenStaffSalaryChanged(Staff staff);

    void updateSalaryDetailsWhenViolationFinesPercentChanged(Violation violation);

    List<TotalSalaryByMonthStats> getSalaryStats();

    List<SalaryDetail> findByMonth(String month);

    SalaryDetail findByStaffIdAndMonth(Integer id, String month);

    List<SalaryDetail> findByStaffId(Integer id);
}
