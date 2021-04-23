package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.SalaryDifferenceDTO;
import com.codegym.restaurant.exception.ScheduleNotFoundException;
import com.codegym.restaurant.model.hr.Schedule;
import com.codegym.restaurant.model.hr.ScheduleDetail;
import com.codegym.restaurant.repository.ScheduleDetailRepository;
import com.codegym.restaurant.repository.ScheduleRepository;
import com.codegym.restaurant.service.SalaryDetailService;
import com.codegym.restaurant.service.ScheduleService;
import com.codegym.restaurant.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleDetailRepository scheduleDetailRepository;

    @Autowired
    private SalaryDetailService salaryDetailService;

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
        Map<Integer, SalaryDifferenceDTO> salaryMap = new HashMap<>();

        Schedule saved = scheduleRepository.save(schedule);
        List<ScheduleDetail> scheduleDetails = saved.getScheduleDetails();
        for (ScheduleDetail s : scheduleDetails) {
            s.setSchedule(saved);
            // build salary map
            Integer staffId = s.getStaff().getId();
            SalaryDifferenceDTO dto = getSalaryDifference(null, s);
            salaryMap.put(staffId, dto);
        }

        scheduleDetailRepository.saveAll(scheduleDetails);

        // TODO: goi service Salary, dung salaryMap de cap nhap
        salaryDetailService.updateSalaryWhenScheduleChanged(schedule.getDate(), salaryMap);
        return saved;
    }

    @Override
    public Schedule update(Schedule schedule) {
        Schedule found = getById(schedule.getId());
        Map<Integer, SalaryDifferenceDTO> salaryMap = new HashMap<>();
        // map theo ID nhan vien cap nhat
        Map<Integer, ScheduleDetail> staffDetailMap = schedule.getScheduleDetails()
                .stream()
                .collect(Collectors.toMap(sd -> sd.getStaff().getId(), Function.identity()));
        // list Details goc & list chuan bi xoa
        List<ScheduleDetail> originalDetails = found.getScheduleDetails();
        List<ScheduleDetail> deletedDetails = new ArrayList<>();
        for (ScheduleDetail original : originalDetails) {
            Integer staffId = original.getStaff().getId(); //idStaff old
            // lich lam viec moi cua nhan vien
            ScheduleDetail newSchedule = staffDetailMap.get(staffId);  //so sanh va tim kiem kiem tra co hay khoong
            SalaryDifferenceDTO dto;
            if (newSchedule == null) {  // cu co moi ko co
                // nhan vien khong co trong lich lam viec -> chuan bi de xoa
                dto = getSalaryDifference(original, null);
                deletedDetails.add(original);
            } else { //chi can cap nhat(moi co cu ko co)
                // cap nhat lich lam viec cua nhan vien va xoa doi tuong trong map
                dto = getSalaryDifference(original, newSchedule);
                dto.setNewViolation(newSchedule.getViolation());

                original.setOvertimeHours(newSchedule.getOvertimeHours());
                original.setViolation(newSchedule.getViolation());
                staffDetailMap.remove(staffId);  //cap nhat song thi xoa luon trong mapNew
            }
            salaryMap.put(staffId, dto);
            //phan con lai trong map la phan them moi
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

                Integer staffId = sd.getStaff().getId();
                SalaryDifferenceDTO dto = getSalaryDifference(null, sd);
                salaryMap.put(staffId, dto);
            }
            scheduleDetailRepository.saveAll(newDetails);
        }

        // TODO: goi service Salary, dung salaryMap de cap nhap
        salaryDetailService.updateSalaryWhenScheduleChanged(schedule.getDate(), salaryMap);
        return scheduleRepository.save(found);
    }

    @Override
    public void deleteById(Integer id) {
        Schedule schedule = getById(id);
        Map<Integer, SalaryDifferenceDTO> salaryMap = new HashMap<>();

        List<ScheduleDetail> details = schedule.getScheduleDetails();
        for (ScheduleDetail s : details) {
            Integer staffId = s.getStaff().getId();
            SalaryDifferenceDTO dto = getSalaryDifference(s, null);
            salaryMap.put(staffId, dto);
        }

        // TODO: goi service Salary, dung salaryMap de cap nhap
        salaryDetailService.updateSalaryWhenScheduleChanged(schedule.getDate(), salaryMap);
        scheduleRepository.deleteById(id);
    }

    public Map<LocalDate, List<Schedule>> findSchedulesOfMonth(String yearMonth) {
        LocalDate firstDate = dateUtils.getFirstDateOfMonth(yearMonth);
        LocalDate endDate = dateUtils.getLastDateOfMonth(yearMonth);

        List<Schedule> scheduleListOfMonth = scheduleRepository.scheduleOfMonth(firstDate, endDate);

        Map<LocalDate, List<Schedule>> schedulesMonth = new TreeMap<>();

        for (Schedule currentSchedule : scheduleListOfMonth) {
            LocalDate currentDate = currentSchedule.getDate();
            List<Schedule> schedules = schedulesMonth.get(currentDate);
            if (schedules == null)
                schedules = new ArrayList<>();
            schedules.add(currentSchedule);
            schedulesMonth.put(currentDate, schedules);
        }
        return schedulesMonth;
    }

    private SalaryDifferenceDTO getSalaryDifference(ScheduleDetail oldSchedule, ScheduleDetail newSchedule) {
        SalaryDifferenceDTO dto = new SalaryDifferenceDTO();
        if (newSchedule != null) {
            dto.setNumberOfShift(1);
            dto.setOvertimeHours(newSchedule.getOvertimeHours());
            dto.setNewViolation(newSchedule.getViolation());
        }

        if (oldSchedule != null) {
            dto.setNumberOfShift(dto.getNumberOfShift() - 1);
            dto.setOvertimeHours(dto.getOvertimeHours() - oldSchedule.getOvertimeHours());
            dto.setOldViolation(oldSchedule.getViolation());
        }

        return dto;
    }
}

//    // map theo ID nhan vien cap nhat
//    Map<Integer, ScheduleDetail> staffDetailMap = schedule.getScheduleDetails()
//            .stream()
//            .collect(Collectors.toMap(sd -> sd.getStaff().getId(), Function.identity()));
//
//    // list Details goc & list chuan bi xoa
//    List<ScheduleDetail> originalDetails = found.getScheduleDetails();
//    List<ScheduleDetail> deletedDetails = new ArrayList<>();
//
//        for (ScheduleDetail original : originalDetails) {
//                Integer staffId = original.getStaff().getId();
//                // lich lam viec moi cua nhan vien
//                ScheduleDetail newSchedule = staffDetailMap.get(staffId);
//                if (newSchedule == null) {
//                // nhan vien khong co trong lich lam viec -> chuan bi de xoa
//                deletedDetails.add(original);
//                } else {
//                // cap nhat lich lam viec cua nhan vien va xoa doi tuong trong map
//                original.setOvertimeHours(newSchedule.getOvertimeHours());
//                original.setViolation(newSchedule.getViolation());
//                staffDetailMap.remove(staffId);
//                }
//                }
//                // doi tuong trong list delete -> xoa
//                scheduleDetailRepository.deleteAll(deletedDetails);
//                originalDetails.removeAll(deletedDetails);
//                // doi tuong con lai trong map la doi tuong moi
//                Collection<ScheduleDetail> newDetails = staffDetailMap.values();
//        if (newDetails.size() > 0) {
//        for (ScheduleDetail sd : newDetails) {
//        sd.setSchedule(found);
//        originalDetails.add(sd);
//        }
//        scheduleDetailRepository.saveAll(newDetails);
//        }

// V2

//@Service
//@Transactional
//public class ScheduleServiceImpl implements ScheduleService {
//    @Autowired
//    private ScheduleRepository scheduleRepository;
//
//    @Autowired
//    private ScheduleDetailRepository scheduleDetailRepository;
//
//    @Autowired
//    private DateUtils dateUtils;
//
//    @Override
//    public List<Schedule> getAll() {
//        return scheduleRepository.findAll();
//    }
//
//    @Override
//    public Schedule getById(Integer id) {
//        return scheduleRepository.findById(id)
//                .orElseThrow(() -> new ScheduleNotFoundException("Ca làm việc không tồn tại"));
//    }
//
//    @Override
//    public Schedule create(Schedule schedule) {
//        Map<Integer, SalaryDifferenceDTO> salaryMap = new HashMap<>();
//
//        Schedule saved = scheduleRepository.save(schedule);
//        List<ScheduleDetail> scheduleDetails = saved.getScheduleDetails();
//        for (ScheduleDetail s : scheduleDetails) {
//            s.setSchedule(saved);
//            // build salary map
//            Integer staffId = s.getStaff().getId();
//
//            SalaryDifferenceDTO dto = new SalaryDifferenceDTO();
//            dto.setNumberOfShift(1);
//            dto.setOvertimeHours(s.getOvertimeHours());
//            dto.setSalaryPerShift(s.getStaff().getSalaryPerShift());
//            dto.setNewViolation(s.getViolation());
//
//            salaryMap.put(staffId, dto);
//        }
//
//        scheduleDetailRepository.saveAll(scheduleDetails);
//
//        // TODO: goi service Salary, dung salaryMap de cap nhap
//        return saved;
//    }
//
//    @Override
//    public Schedule update(Schedule schedule) {
//        Schedule found = getById(schedule.getId());
//
//        Map<Integer, SalaryDifferenceDTO> salaryMap = new HashMap<>();
//
//        // map theo ID nhan vien cap nhat
//        Map<Integer, ScheduleDetail> staffDetailMap = schedule.getScheduleDetails()
//                .stream()
//                .collect(Collectors.toMap(sd -> sd.getStaff().getId(), Function.identity()));
//
//        // list Details goc & list chuan bi xoa
//        List<ScheduleDetail> originalDetails = found.getScheduleDetails();
//        List<ScheduleDetail> deletedDetails = new ArrayList<>();
//
//        for (ScheduleDetail original : originalDetails) {
//            Integer staffId = original.getStaff().getId(); //idStaff old
//            // lich lam viec moi cua nhan vien
//            ScheduleDetail newSchedule = staffDetailMap.get(staffId);  //so sanh va tim kiem kiem tra co hay khoong
//            if (newSchedule == null) {  // cu co moi ko co
//                // nhan vien khong co trong lich lam viec -> chuan bi de xoa
//                SalaryDifferenceDTO dto = new SalaryDifferenceDTO();
//                dto.setNumberOfShift(-1);
//                dto.setOvertimeHours(-original.getOvertimeHours());
//                dto.setSalaryPerShift(original.getStaff().getSalaryPerShift());
//                dto.setOldViolation(original.getViolation());
//                salaryMap.put(staffId, dto);
//
//                deletedDetails.add(original);
//            } else { //chi can cap nhat(moi co cu ko co)
//                // cap nhat lich lam viec cua nhan vien va xoa doi tuong trong map
//
//                SalaryDifferenceDTO dto = new SalaryDifferenceDTO();
//                dto.setOvertimeHours(newSchedule.getOvertimeHours() - original.getOvertimeHours());
//                dto.setOldViolation(original.getViolation());
//                dto.setNewViolation(newSchedule.getViolation());
//
//                original.setOvertimeHours(newSchedule.getOvertimeHours());
//                original.setViolation(newSchedule.getViolation());
//                staffDetailMap.remove(staffId);  //cap nhat song thi xoa luon trong mapNew
//            }
//            //phan con lai trong map la phan them moi
//        }
//        // doi tuong trong list delete -> xoa
//        scheduleDetailRepository.deleteAll(deletedDetails);
//        originalDetails.removeAll(deletedDetails);
//        // doi tuong con lai trong map la doi tuong moi
//        Collection<ScheduleDetail> newDetails = staffDetailMap.values();
//        if (newDetails.size() > 0) {
//            for (ScheduleDetail sd : newDetails) {
//                sd.setSchedule(found);
//                originalDetails.add(sd);
//
//                Integer staffId = sd.getStaff().getId();
//                SalaryDifferenceDTO dto = new SalaryDifferenceDTO();
//                dto.setNumberOfShift(1);
//                dto.setSalaryPerShift(sd.getStaff().getSalaryPerShift());
//                dto.setOvertimeHours(sd.getOvertimeHours());
//                dto.setNewViolation(sd.getViolation());
//
//                salaryMap.put(staffId, dto);
//            }
//            scheduleDetailRepository.saveAll(newDetails);
//        }
//
//        // TODO: goi service Salary, dung salaryMap de cap nhap
//        return scheduleRepository.save(found);
//    }
//
//    @Override
//    public void deleteById(Integer id) {
//        Schedule found = getById(id);
//        Map<Integer, SalaryDifferenceDTO> salaryMap = new HashMap<>();
//
//        List<ScheduleDetail> details = found.getScheduleDetails();
//        for (ScheduleDetail s : details) {
//            Integer staffId = s.getStaff().getId();
//
//            SalaryDifferenceDTO dto = new SalaryDifferenceDTO();
//            dto.setNumberOfShift(-1);
//            dto.setOvertimeHours(-s.getOvertimeHours());
//            dto.setSalaryPerShift(s.getStaff().getSalaryPerShift());
//            dto.setOldViolation(s.getViolation());
//
//            salaryMap.put(staffId, dto);
//        }
//
//        // TODO: goi service Salary, dung salaryMap de cap nhap
//        scheduleRepository.deleteById(id);
//    }
//
//
//    public Map<LocalDate, List<Schedule>> findSchedulesOfMonth(String yearMonth) {
//        LocalDate firstDate = dateUtils.getFirstDateOfMonth(yearMonth);
//        LocalDate endDate = dateUtils.getLastDateOfMonth(yearMonth);
//        List<Schedule> scheduleListOfMonth = scheduleRepository.scheduleOfMonth(firstDate, endDate);
//        Map<LocalDate, List<Schedule>> schedulesMonth = new TreeMap<>();
//        for (Schedule currentSchedule : scheduleListOfMonth) {
//            LocalDate currentDate = currentSchedule.getDate();
//            List<Schedule> schedules = schedulesMonth.get(currentDate);
//            if (schedules == null)
//                schedules = new ArrayList<>();
//            schedules.add(currentSchedule);
//            schedulesMonth.put(currentDate, schedules);
//        }
//        return schedulesMonth;
//    }
//}