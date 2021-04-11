package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaSoftDeleteRepository<Role, Integer> {
}
