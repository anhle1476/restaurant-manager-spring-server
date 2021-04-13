package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.hr.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
}
