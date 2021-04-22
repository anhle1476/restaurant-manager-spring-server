package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.TotalSalaryByMonthStats;
import com.codegym.restaurant.model.hr.SalaryDetail;
import com.codegym.restaurant.service.SalaryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/salaries")
public class SalaryController {
    @Autowired
    private SalaryDetailService salaryDetailService;

    @GetMapping
    public ResponseEntity<List<TotalSalaryByMonthStats>> getSalaryStats() {
        return new ResponseEntity<>(salaryDetailService.getSalaryStats(), HttpStatus.OK);
    }

    @GetMapping("/{month}")
    public ResponseEntity<List<SalaryDetail>> getSalaryByMonth(@PathVariable("month") String month) {
        return new ResponseEntity<>(salaryDetailService.findByMonth(month), HttpStatus.OK);
    }

    @GetMapping("/{month}/staffs/{staffId}")
    public ResponseEntity<SalaryDetail> getStaffSalaryByMonth(
            @PathVariable("month") String month,
            @PathVariable("staffId") Integer staffId
    ) {
        return new ResponseEntity<>(salaryDetailService.findByStaffIdAndMonth(staffId,month), HttpStatus.OK);
    }
}
