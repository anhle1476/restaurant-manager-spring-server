package com.codegym.restaurant.service;

import com.codegym.restaurant.model.hr.Schedule;

import java.util.List;

public interface ScheduleService {
    List<Schedule> getAll();
    Schedule getById(Integer integer);
    Schedule create(Schedule schedule);
    Schedule update(Schedule schedule);
    void deleteById(Integer id);
}
