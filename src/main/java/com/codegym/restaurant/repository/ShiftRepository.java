package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.Shift;

import java.util.List;

public interface ShiftRepository extends JpaSoftDeleteRepository<Shift, Integer> {

    List<Shift> findAllByDeletedIsTrue();
}
