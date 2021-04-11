package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.Staff;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaSoftDeleteRepository<Staff, Integer> {

    Optional<Staff> findByUsername(String username);
}
