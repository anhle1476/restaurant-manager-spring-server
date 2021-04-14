package com.codegym.restaurant.service;

import com.codegym.restaurant.model.hr.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface ScheduleService extends SchedulerBaseService<Schedule, Integer> {

    Map<LocalDate, List<Schedule>> findSchedulesOfMonth(String yearMonth);
}
