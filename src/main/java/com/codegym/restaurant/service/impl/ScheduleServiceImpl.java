package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.ScheduleNotFoundException;
import com.codegym.restaurant.model.hr.Schedule;
import com.codegym.restaurant.repository.ScheduleRepository;
import com.codegym.restaurant.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<Schedule> getAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule getById(Integer id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Ca làm việc không tồn tại"));
    }

    @Override
    public Schedule create(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule update(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public void deleteById(Integer id){
        scheduleRepository.deleteById(id);
    }
}
