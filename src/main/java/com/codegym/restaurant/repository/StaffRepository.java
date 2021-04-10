package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.Staff;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaSoftDeleteRepository<Staff, Integer> {
}
