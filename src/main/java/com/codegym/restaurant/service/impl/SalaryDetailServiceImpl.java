package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.SalaryDifferenceDTO;
import com.codegym.restaurant.repository.SalaryRepository;
import com.codegym.restaurant.repository.ScheduleDetailRepository;
import com.codegym.restaurant.service.SalaryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Map;



@Service
@Transactional
public class SalaryDetailServiceImpl implements SalaryDetailService {
    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private ScheduleDetailRepository scheduleDetailRepository;

    @Override
    public void salaryDetailsWithStaff(LocalDate firstDateOfMonth, Map<Integer, SalaryDifferenceDTO> differenceMap) {
    }
}
