package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.StaffInfoDTO;
import com.codegym.restaurant.model.hr.Staff;

import java.util.List;

public interface StaffService{
    List<StaffInfoDTO> getAll();
    List<StaffInfoDTO> getAllDeleted();
    StaffInfoDTO getById(Integer integer);
    StaffInfoDTO create(Staff staff);
    StaffInfoDTO update(Staff staff);
    void delete(Integer integer);
    void restore(Integer integer);
}
