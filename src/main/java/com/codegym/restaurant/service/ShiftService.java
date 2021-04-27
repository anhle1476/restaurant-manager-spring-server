package com.codegym.restaurant.service;

import com.codegym.restaurant.model.hr.Shift;

import java.util.List;

public interface ShiftService extends BaseService<Shift, Integer>{
    List<Shift> getAllWithBothDeletedStatus();
}
