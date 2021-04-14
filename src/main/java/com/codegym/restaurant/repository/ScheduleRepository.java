package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {

    @Query("SELECT s from Schedule s where s.date >= :firstDate and s.date <= :lastDate")
    List<Schedule> scheduleOfMonth(LocalDate firstDate, LocalDate lastDate);
}
