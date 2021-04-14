package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.ScheduleNotFoundException;
import com.codegym.restaurant.model.hr.Schedule;
import com.codegym.restaurant.model.hr.ScheduleDetail;
import com.codegym.restaurant.model.hr.Shift;
import com.codegym.restaurant.repository.ScheduleDetailRepository;
import com.codegym.restaurant.repository.ScheduleRepository;
import com.codegym.restaurant.service.ScheduleService;
import com.codegym.restaurant.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleDetailRepository scheduleDetailRepository;

    @Autowired
    private DateUtils dateUtils;

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
        Schedule saved = scheduleRepository.save(schedule);
        List<ScheduleDetail> scheduleDetails = saved.getScheduleDetails();
        for (ScheduleDetail s : scheduleDetails) {
            s.setSchedule(saved);
        }
        scheduleDetailRepository.saveAll(scheduleDetails);
        return saved;
    }

    @Override
    public Schedule update(Schedule schedule) {
        Schedule found = scheduleRepository.findById(schedule.getId())
                .orElseThrow(() -> new ScheduleNotFoundException("Ca làm việc không tồn tại"));
        // map theo ID nhan vien cap nhat
        Map<Integer, ScheduleDetail> staffDetailMap = schedule.getScheduleDetails()
                .stream()
                .collect(Collectors.toMap(sd -> sd.getStaff().getId(), Function.identity()));

        // list Details goc & list chuan bi xoa
        List<ScheduleDetail> originalDetails = found.getScheduleDetails();
        List<ScheduleDetail> deletedDetails = new ArrayList<>();

        for (ScheduleDetail original : originalDetails) {
            Integer staffId = original.getStaff().getId();
            // lich lam viec moi cua nhan vien
            ScheduleDetail newSchedule = staffDetailMap.get(staffId);
            if (newSchedule == null) {
                // nhan vien khong co trong lich lam viec -> chuan bi de xoa
                deletedDetails.add(original);
            } else {
                // cap nhat lich lam viec cua nhan vien va xoa doi tuong trong map
                original.setOvertimeHours(newSchedule.getOvertimeHours());
                original.setViolation(newSchedule.getViolation());
                staffDetailMap.remove(staffId);
            }
        }
        // doi tuong trong list delete -> xoa
        scheduleDetailRepository.deleteAll(deletedDetails);
        originalDetails.removeAll(deletedDetails);
        // doi tuong con lai trong map la doi tuong moi
        Collection<ScheduleDetail> newDetails = staffDetailMap.values();
        if (newDetails.size() > 0) {
            for (ScheduleDetail sd : newDetails) {
                sd.setSchedule(found);
                originalDetails.add(sd);
            }
            scheduleDetailRepository.saveAll(newDetails);
        }
        return scheduleRepository.save(found);
    }

    @Override
    public void deleteById(Integer id) {
        scheduleRepository.deleteById(id);
    }


    public Map<LocalDate,List<Schedule>> findSchedulesOfMonth(String yearMonth) {
        LocalDate firstDate = dateUtils.getFirstDateOfMonth(yearMonth);
        LocalDate endDate = dateUtils.getLastDateOfMonth(yearMonth);
        List<Schedule> scheduleListOfMonth = scheduleRepository.scheduleOfMonth(firstDate, endDate);
        Map<LocalDate, List<Schedule>> schedulesMonth = new TreeMap<>();
        for (Schedule currentSchedule : scheduleListOfMonth){
            LocalDate currentDate = currentSchedule.getDate();
            List<Schedule> schedules = schedulesMonth.get(currentDate);
            if (schedules == null)
                schedules = new ArrayList<>();
            schedules.add(currentSchedule);
            schedulesMonth.put(currentDate, schedules);
        }
        return schedulesMonth;
    }
}