package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.exception.InvalidScheduleException;
import com.codegym.restaurant.model.hr.Schedule;
import com.codegym.restaurant.service.ScheduleService;
import com.codegym.restaurant.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    public ResponseEntity<List<Schedule>> showSchedule() {
        return new ResponseEntity<>(scheduleService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createSchedule(@Valid @RequestBody Schedule schedule, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);

        try {
            return new ResponseEntity<>(scheduleService.create(schedule), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            throw new InvalidScheduleException("Trong 1 ngày không thể có 2 ca giống nhau");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(
            @Valid @RequestBody Schedule schedule,
            BindingResult result,
            @PathVariable(value = "id") Integer id) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!schedule.getId().equals(id))
            throw new IdNotMatchException("Id không trùng hợp");
        try {
            return new ResponseEntity<>(scheduleService.update(schedule), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            throw new InvalidScheduleException("Trong 1 ngày không thể có 2 ca giống nhau");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
        scheduleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/by-month/{yearMonth}")
    public ResponseEntity<Map<LocalDate, List<Schedule>>> findSchedulesOfMonth(@PathVariable(value = "yearMonth") String yearMonth) {
        return new ResponseEntity<>(scheduleService.findSchedulesOfMonth(yearMonth), HttpStatus.OK);
    }

}
