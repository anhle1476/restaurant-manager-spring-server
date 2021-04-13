package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.Role;
import com.codegym.restaurant.model.hr.Staff;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaSoftDeleteRepository<Role, Integer> {
}
