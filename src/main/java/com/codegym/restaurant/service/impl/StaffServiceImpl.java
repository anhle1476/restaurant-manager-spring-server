package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.UpdateAccountInfoDTO;
import com.codegym.restaurant.dto.UpdateAccountPasswordDTO;
import com.codegym.restaurant.dto.UpdateStaffPasswordDTO;
import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.exception.UpdatePasswordFailedException;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.StaffRepository;
import com.codegym.restaurant.service.SalaryDetailService;
import com.codegym.restaurant.service.StaffService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements UserDetailsService, StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SalaryDetailService salaryDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return staffRepository.findByUsername(username).orElseThrow(
                () -> new StaffNotFoundException(String.format("Tài khoản %s không tồn tại", username)));
    }

    //CRUD
    @Override
    public List<Staff> getAll() {

        return staffRepository.findAllAvailable();
    }

    @Override
    public List<Staff> getAllDeleted() {
        return staffRepository.findAllDeleted();
    }

    @Override
    public Staff getById(Integer id) {
        return staffRepository.findAvailableById(id)
                .orElseThrow(() -> new StaffNotFoundException("Không có tài khoản này"));
    }


    @Override
    public Staff create(Staff staff) {
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        return staffRepository.save(staff);
    }

    @Override
    public Staff update(Staff staff) {
        Staff found = staffRepository.findById(staff.getId())
                .orElseThrow(() -> new StaffNotFoundException("Không thể cập nhật thông tin nhân viên này"));
        found.setFullname(staff.getFullname());
        found.setPhoneNumber(staff.getPhoneNumber());
        found.setRole(staff.getRole());
        // kiem tra luong cua found co giong voi luong cua staff truyen vo khong
        if (found.getSalaryPerShift() != staff.getSalaryPerShift()) {
            found.setSalaryPerShift(staff.getSalaryPerShift());
            salaryDetailService.updateSalaryDetailsWhenStaffSalaryChanged(found);
        }
        return staffRepository.save(found);
    }

    @Override
    public void delete(Integer id) {
        Staff staff = getById(id);
        staff.setDeleted(true);
        staffRepository.save(staff);
    }

    @Override
    public void restore(Integer id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException("Không thể phục hồi nhân viên này"));
        if (!staff.isDeleted())
            throw new EntityRestoreFailedException("không thể phục hồi tài khoản");
        staff.setDeleted(false);
        staffRepository.save(staff);
    }

    @Override
        public void updateStaffPassword(UpdateStaffPasswordDTO updateStaffPasswordDTO) {
        Staff staff = getById(updateStaffPasswordDTO.getStaffId());
        staff.setPassword(passwordEncoder.encode(updateStaffPasswordDTO.getNewPassword()));
        staffRepository.save(staff);
    }

    @Override
    public void updateAccountPassword(Integer accountId, UpdateAccountPasswordDTO updateAccountPasswordDTO) {
        Staff staff = staffRepository.findAvailableById(accountId)
                .orElseThrow(() -> new StaffNotFoundException("Tài khoản đã bị khóa, không thể đổi mật khẩu"));
        boolean matches = passwordEncoder.matches(updateAccountPasswordDTO.getCurrentPassword(), staff.getPassword());
        if (!matches)
            throw new UpdatePasswordFailedException("Sai mật khẩu");

        staff.setPassword(passwordEncoder.encode(updateAccountPasswordDTO.getNewPassword()));
        staffRepository.save(staff);
    }

    @Override
    public Staff updateAccountInfo(Integer accountId, UpdateAccountInfoDTO updateAccountInfoDTO) {
        Staff staff = getById(accountId);
        staff.setFullname(updateAccountInfoDTO.getFullname());
        staff.setPhoneNumber(updateAccountInfoDTO.getPhoneNumber());
        return staffRepository.save(staff);
    }
}
