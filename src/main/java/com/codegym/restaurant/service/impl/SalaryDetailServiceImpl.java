package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.SalaryDifferenceDTO;
import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.exception.ViolationNotFoundException;
import com.codegym.restaurant.model.hr.SalaryDetail;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.model.hr.Violation;
import com.codegym.restaurant.model.hr.ViolationDetail;
import com.codegym.restaurant.repository.SalaryDetailRepository;
import com.codegym.restaurant.repository.ScheduleDetailRepository;
import com.codegym.restaurant.repository.StaffRepository;
import com.codegym.restaurant.repository.ViolationRepository;
import com.codegym.restaurant.service.SalaryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Override
    public void updateSalaryWhenScheduleChange(LocalDate firstDateOfMonth, Map<Integer, SalaryDifferenceDTO> differenceMap) {
        Set<Integer> idStaffs = differenceMap.keySet();
        LocalDate firstDate = getFirstDate(firstDateOfMonth);

        List<SalaryDetail> currentSalaryDetails = salaryDetailRepository.salaryDetailsWithStaff(idStaffs, firstDate);
        for (SalaryDetail salaryDetail : currentSalaryDetails) {
            Integer staffId = salaryDetail.getStaff().getId();
            SalaryDifferenceDTO dto = differenceMap.get(staffId);
            // cap nhat so ca va thoi gian qua gio
            salaryDetail.setNumberOfShift(salaryDetail.getNumberOfShift() + dto.getNumberOfShift());
            salaryDetail.setTotalOvertimeHours(salaryDetail.getTotalOvertimeHours() + dto.getOvertimeHours());
            // kiem tra neu violationDetail rong -> tao list moi
            List<ViolationDetail> violationDetailList = salaryDetail.getViolationDetails();
            if (violationDetailList == null) {
                violationDetailList = new ArrayList<>();
                salaryDetail.setViolationDetails(violationDetailList);
            }
            // cap nhat loi cu va loi moi trong danh sach loi hien tai
            boolean isOldViolationNotEmpty = dto.getOldViolation() != null;
            boolean isNewViolationNotEmpty = dto.getNewViolation() != null;
            boolean hasNewViolation = false;
            for (ViolationDetail violationDetail : violationDetailList) {
                Integer violationId = violationDetail.getViolation().getId();
                // kiem tra va cap nhat loi cu
                if (isOldViolationNotEmpty && dto.getOldViolation().getId() == violationId) {
                    violationDetail.setNumberOfViolations(violationDetail.getNumberOfViolations() - 1);
                }
                // kiem tra va cap nhat loi moi
                if (isNewViolationNotEmpty && dto.getNewViolation().getId() == violationId) {
                    hasNewViolation = true;
                    violationDetail.setNumberOfViolations(violationDetail.getNumberOfViolations() + 1);
                }
            }
            // neu loi moi khong co trong danh sach hien tai -> tao loi va them vao danh sach
            if (isNewViolationNotEmpty && !hasNewViolation) {
                ViolationDetail detail = createViolationDetail(dto);
                detail.setSalaryDetail(salaryDetail);
                violationDetailList.add(detail);
            }
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
            salaryDetail.setFirstDateOfMonth(firstDate);
            salaryDetail.setTotalOvertimeHours(dto.getOvertimeHours());
            salaryDetail.setNumberOfShift(1);

            if (dto.getNewViolation() != null) {
                ViolationDetail violationDetail = createViolationDetail(dto);
                violationDetail.setSalaryDetail(salaryDetail);
                List<ViolationDetail> violationList = new ArrayList<>();
                violationList.add(violationDetail);
                salaryDetail.setViolationDetails(violationList);
            }

            // them salaryDetail vao list hien tai
            currentSalaryDetails.add(salaryDetail);
        }
        // cap nhat luong
        for (SalaryDetail salaryDetail : currentSalaryDetails) {
            long salaryPerShift = salaryDetail.getStaff().getSalaryPerShift();
            float numberOfShift = (float) salaryDetail.getNumberOfShift();
            float totalFinesPercent = 0;
            List<ViolationDetail> violationDetails = salaryDetail.getViolationDetails();
            if (violationDetails != null) {
                for (ViolationDetail violationDetail : violationDetails)
                    totalFinesPercent = violationDetail.getNumberOfViolations() * ((float) violationDetail.getFinesPercent() / 100);
            }
            long finalSalary = (long) (salaryPerShift * (numberOfShift - totalFinesPercent));
            salaryDetail.setSalary(finalSalary);
        }
        salaryDetailRepository.saveAll(currentSalaryDetails);
    }

    private ViolationDetail createViolationDetail(SalaryDifferenceDTO differenceDTO) {
        Violation violation = violationRepository.findById(differenceDTO.getNewViolation().getId())
                .orElseThrow(() -> new ViolationNotFoundException("Vi phạm không tồn tại"));
        ViolationDetail detail = new ViolationDetail();
        detail.setViolation(violation);
        detail.setFinesPercent(violation.getFinesPercent());
        detail.setNumberOfViolations(1);
        return detail;
    }

    private LocalDate getFirstDate(LocalDate dates) {
        return LocalDate.of(dates.getYear(), dates.getMonth(), 1);
    }
}
