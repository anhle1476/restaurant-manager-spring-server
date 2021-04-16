package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.SalaryDetail;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface SalaryRepository extends JpaSoftDeleteRepository<SalaryDetail, Integer> {
    @Query("select s from SalaryDetail s where s.staff.id IN (:ids) and s.firstDateOfMonth = (:firstDate)")
    List<SalaryDetail> salaryDetailsWithStaff(Collection<Integer> ids, LocalDate firstDate);

}
