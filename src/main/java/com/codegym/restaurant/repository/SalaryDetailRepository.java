package com.codegym.restaurant.repository;

import com.codegym.restaurant.dto.TotalSalaryByMonthStats;
import com.codegym.restaurant.model.hr.SalaryDetail;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SalaryDetailRepository extends JpaSoftDeleteRepository<SalaryDetail, Integer> {
    @Query("select s from SalaryDetail s where s.staff.id IN (:ids) and s.firstDateOfMonth = (:firstDate)")
    List<SalaryDetail> findSalaryDetailOfMonthWithStaffIds(Collection<Integer> ids, LocalDate firstDate);

    @Query("select s from SalaryDetail s where s.staff.id = :id and s.firstDateOfMonth >= :firstDate")
    List<SalaryDetail> findSalaryDetailsOfStaffFromThisMonth(Integer id, LocalDate firstDate);

    @Query("select s from SalaryDetail s where s.firstDateOfMonth >= :firstDate")
    List<SalaryDetail> findSalaryDetailsFromThisMonth(LocalDate firstDate);

    @Query("select s.firstDateOfMonth as firstDateOfMonth, " +
            "sum(s.salary) as totalSalary, " +
            "count(s.id) as totalStaff " +
            "from SalaryDetail s " +
            "where s.salary > 0 " +
            "group by s.firstDateOfMonth")
    List<TotalSalaryByMonthStats> getSalaryStats();

    List<SalaryDetail> findByFirstDateOfMonth(LocalDate firstDateOfMonth);

    Optional<SalaryDetail> findByStaffIdAndFirstDateOfMonth(Integer id, LocalDate firstDateOfMonth);

    List<SalaryDetail> findByStaffId(Integer id);
}
