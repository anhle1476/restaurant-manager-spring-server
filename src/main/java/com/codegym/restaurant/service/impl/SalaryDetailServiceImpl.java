package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.SalaryDifferenceDTO;
import com.codegym.restaurant.dto.TotalSalaryByMonthStats;
import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.exception.ViolationNotFoundException;
import com.codegym.restaurant.model.hr.SalaryDetail;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.model.hr.Violation;
import com.codegym.restaurant.model.hr.ViolationDetail;
import com.codegym.restaurant.repository.SalaryDetailRepository;
import com.codegym.restaurant.repository.StaffRepository;
import com.codegym.restaurant.repository.ViolationRepository;
import com.codegym.restaurant.service.SalaryDetailService;
import com.codegym.restaurant.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class SalaryDetailServiceImpl implements SalaryDetailService {
    @Autowired
    private SalaryDetailRepository salaryDetailRepository;

    @Autowired
    private ViolationRepository violationRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private DateUtils dateUtils;

    @Override
    public void updateSalaryWhenScheduleChanged(LocalDate scheduleDate, Map<Integer, SalaryDifferenceDTO> differenceMap) {
        Set<Integer> newScheduleStaffIds = differenceMap.keySet();
        LocalDate firstDateOfMonth = dateUtils.getFirstDateOfMonth(scheduleDate);

        List<SalaryDetail> currentSalaryDetails = salaryDetailRepository.findSalaryDetailOfMonthWithStaffIds(newScheduleStaffIds, firstDateOfMonth);
        // cap nhat cac staff hien tai da co luong trong thang
        for (SalaryDetail salaryDetail : currentSalaryDetails) {
            Integer staffId = salaryDetail.getStaff().getId();
            SalaryDifferenceDTO dto = differenceMap.get(staffId);
            // cap nhat so ca va thoi gian qua gio, chi tiet vi pham
            salaryDetail.addNumberOfShift(dto.getNumberOfShift());
            salaryDetail.addOvertimeHours(dto.getOvertimeHours());
            updateViolationDetailsByDifferenceDTO(salaryDetail, dto.getOldViolation(), dto.getNewViolation());
            // xoa dto khoi differenceMap -> con lai trong map la salaryDetail moi
            differenceMap.remove(staffId);
        }
        // voi nhung staff chua co luong trong thang -> khong co trong currentSalaryDetails -> tao moi
        for (Integer staffId : differenceMap.keySet()) {
            Staff staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new StaffNotFoundException("Nhân viên không tồn tại"));
            SalaryDifferenceDTO dto = differenceMap.get(staffId);
            // tao salaryDetail moi
            SalaryDetail salaryDetail = new SalaryDetail();
            salaryDetail.setStaff(staff);
            salaryDetail.setFirstDateOfMonth(firstDateOfMonth);
            salaryDetail.setNumberOfShift(1);
            salaryDetail.setTotalOvertimeHours(dto.getOvertimeHours());

            if (dto.getNewViolation() != null) {
                ViolationDetail violationDetail = createNewViolationDetail(dto.getNewViolation());
                violationDetail.setSalaryDetail(salaryDetail);
                List<ViolationDetail> violationList = new ArrayList<>();
                violationList.add(violationDetail);
                salaryDetail.setViolationDetails(violationList);
            }
            // them salaryDetail vao list hien tai
            currentSalaryDetails.add(salaryDetail);
        }
        // cap nhat luong
        updateSalaryPerShiftAndTotalSalary(currentSalaryDetails);
        salaryDetailRepository.saveAll(currentSalaryDetails);
    }

    private void updateViolationDetailsByDifferenceDTO(SalaryDetail salaryDetail, Violation oldViolation, Violation newViolation) {
        // kiem tra neu violationDetail rong -> tao list moi
        List<ViolationDetail> violationDetails = salaryDetail.getViolationDetails();
        if (violationDetails == null) {
            violationDetails = new ArrayList<>();
            salaryDetail.setViolationDetails(violationDetails);
        }
        // cap nhat loi cu va loi moi trong danh sach loi hien tai
        boolean hasNewViolation = false;
        for (ViolationDetail violationDetail : violationDetails) {
            Integer currentViolationId = violationDetail.getViolation().getId();
            // kiem tra va cap nhat loi cu (tru di 1 lan vi pham)
            if (isTheSameViolation(oldViolation, currentViolationId)) {
                violationDetail.decreaseNumberOfViolations();
            }
            // kiem tra va cap nhat loi moi (tang them 1 lan vi pham)
            if (isTheSameViolation(newViolation, currentViolationId)) {
                violationDetail.increaseNumberOfViolations();
                hasNewViolation = true;
            }
        }
        // neu loi moi khong co trong danh sach hien tai -> tao loi va them vao danh sach
        if (newViolation != null && !hasNewViolation) {
            ViolationDetail detail = createNewViolationDetail(newViolation);
            detail.setSalaryDetail(salaryDetail);
            violationDetails.add(detail);
        }
    }

    private boolean isTheSameViolation(Violation violation, Integer currentViolationId) {
        return violation != null && violation.getId().equals(currentViolationId);
    }

    private ViolationDetail createNewViolationDetail(Violation newViolation) {
        Violation violation = violationRepository.findById(newViolation.getId())
                .orElseThrow(() -> new ViolationNotFoundException("Vi phạm không tồn tại"));
        ViolationDetail detail = new ViolationDetail();
        detail.setViolation(violation);
        detail.setFinesPercent(violation.getFinesPercent());
        detail.setNumberOfViolations(1);
        return detail;
    }

    @Override
    public void updateSalaryDetailsWhenStaffSalaryChanged(Staff staff) {
        LocalDate firstDateOfMonth = getFirstDateOfCurrentMonth();
        List<SalaryDetail> salaryDetails = salaryDetailRepository.findSalaryDetailsOfStaffFromThisMonth(staff.getId(), firstDateOfMonth);
        updateSalaryPerShiftAndTotalSalary(salaryDetails);
        salaryDetailRepository.saveAll(salaryDetails);
    }

    @Override
    public void updateSalaryDetailsWhenViolationFinesPercentChanged(Violation violation) {
        Integer updatedId = violation.getId();
        int updatedFinesPercent = violation.getFinesPercent();
        // tim tat ca salaryDetails tu thang nay tro ve sau
        LocalDate firstDateOfMonth = getFirstDateOfCurrentMonth();
        List<SalaryDetail> salaryDetails = salaryDetailRepository.findSalaryDetailsFromThisMonth(firstDateOfMonth);
        for (SalaryDetail salaryDetail : salaryDetails) {
            List<ViolationDetail> violationDetails = salaryDetail.getViolationDetails();
            if (violationDetails == null)
                continue;
            // kiem tra neu salaryDetail co chua vi pham moi duoc cap nhat -> cap nhat muc phat va tong luong
            boolean containsUpdatedViolation = false;
            for (ViolationDetail violationDetail : violationDetails) {
                if (violationDetail.getViolation().getId().equals(updatedId)) {
                    violationDetail.setFinesPercent(updatedFinesPercent);
                    containsUpdatedViolation = true;
                    break;
                }
            }
            if (containsUpdatedViolation) {
                updateTotalSalary(salaryDetail);
                salaryDetailRepository.save(salaryDetail);
            }
        }
    }

    @Override
    public List<TotalSalaryByMonthStats> getSalaryStats() {
        return salaryDetailRepository.getSalaryStats();
    }

    @Override
    public List<SalaryDetail> findByMonth(String month) {
        LocalDate firstDateOfMonth = dateUtils.getFirstDateOfMonth(month);
        return salaryDetailRepository.findByFirstDateOfMonth(firstDateOfMonth);
    }

    @Override
    public SalaryDetail findByStaffIdAndMonth(Integer id, String month) {
        LocalDate firstDateOfMonth = dateUtils.getFirstDateOfMonth(month);
        return salaryDetailRepository.findByStaffIdAndFirstDateOfMonth(id, firstDateOfMonth)
                .orElseThrow(() -> new IllegalStateException(""));
    }

    @Override
    public List<SalaryDetail> findByStaffId(Integer id) {
        return salaryDetailRepository.findByStaffId(id);
    }

    private void updateSalaryPerShiftAndTotalSalary(List<SalaryDetail> salaryDetails) {
        for (SalaryDetail salaryDetail : salaryDetails) {
            updateSalaryPerShift(salaryDetail);
            updateTotalSalary(salaryDetail);
        }
    }

    private void updateSalaryPerShift(SalaryDetail salaryDetail) {
        long salaryPerShift = salaryDetail.getStaff().getSalaryPerShift();
        salaryDetail.setCurrentSalaryPerShift(salaryPerShift);
    }

    private void updateTotalSalary(SalaryDetail salaryDetail) {
        float numberOfShift = (float) salaryDetail.getNumberOfShift();
        float totalFinesPercent = getTotalFinesPercent(salaryDetail);
        long finalSalary = (long) (salaryDetail.getCurrentSalaryPerShift() * (numberOfShift - totalFinesPercent));
        salaryDetail.setSalary(finalSalary);
    }

    private float getTotalFinesPercent(SalaryDetail salaryDetail) {
        List<ViolationDetail> violationDetails = salaryDetail.getViolationDetails();
        if (violationDetails == null)
            return 0;

        float totalFinesPercent = 0;
        for (ViolationDetail violationDetail : violationDetails) {
            totalFinesPercent = violationDetail.getNumberOfViolations() * ((float) violationDetail.getFinesPercent() / 100);
        }
        return totalFinesPercent;
    }

    private LocalDate getFirstDateOfCurrentMonth() {
        return dateUtils.getFirstDateOfMonth(LocalDate.now());
    }
}
