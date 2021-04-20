package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaSoftDeleteRepository<Staff, Integer> {
    Optional<Staff> findByUsername(String username);

    @Query("SELECT s FROM Staff s " +
            "WHERE s.deleted = FALSE " +
            "AND s.role.deleted = FALSE " +
            "AND s.role.id = :id")
    List<Staff> findStaffByRoleId(Integer id);
}
