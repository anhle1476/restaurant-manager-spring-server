package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.SalaryDetail;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface SalaryDetailRepository extends JpaSoftDeleteRepository<SalaryDetail, Integer> {
    @Query("select s from SalaryDetail s where s.staff.id IN (:ids) and s.firstDateOfMonth = (:firstDate)")
    List<SalaryDetail> findSalaryDetailOfMonthWithStaffIds(Collection<Integer> ids, LocalDate firstDate);

    @Query("select s from SalaryDetail s where s.staff.id = :id and s.firstDateOfMonth >= :firstDate")
    List<SalaryDetail> findSalaryDetailsOfStaffFromThisMonth(Integer id, LocalDate firstDate);

    @Query("select s from SalaryDetail s where s.firstDateOfMonth >= :firstDate")
    List<SalaryDetail> findSalaryDetailsFromThisMonth(LocalDate firstDate);
}
