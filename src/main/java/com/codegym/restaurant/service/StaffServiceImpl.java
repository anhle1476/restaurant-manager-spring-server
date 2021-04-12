package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.StaffInfoDTO;
import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.StaffRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements UserDetailsService, StaffService {

    @Autowired
    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return staffRepository.findByUsername(username).orElseThrow(
                () -> new StaffNotFoundException(String.format("Tài khoản %s không tồn tại", username)));
    }

    //CRUD

    @Override
    public List<StaffInfoDTO> getAll() {
        return staffRepository.findAllStaffByDeletedIsFalse();
    }

    @Override
    public List<StaffInfoDTO> getAllDeleted() {
        return staffRepository.findAllStaffByDeletedIsTrue();
    }

    @Override
    public StaffInfoDTO getById(Integer integer) {
        return staffRepository.findStaffById(integer)
                .orElseThrow(() -> new StaffNotFoundException("Không có tài khoản này"));
    }


    @Override
    public StaffInfoDTO create(Staff staff) {
        Staff saved = staffRepository.save(staff);
        return staffRepository.findStaffById(saved.getId()).orElseThrow(() -> new StaffNotFoundException("Tạo TK thất bại"));
    }

    // cá độ huy anh xem pasword co null ko.
    @Override
    public StaffInfoDTO update(Staff staff) {
        Staff found = staffRepository.findById(staff.getId())
                .orElseThrow(() -> new StaffNotFoundException("Khong co"));
        found.setFullname(staff.getFullname());
        found.setPhoneNumber(staff.getPhoneNumber());
        found.setSalaryPerShift(staff.getSalaryPerShift());
        found.setRole(staff.getRole());
        staffRepository.save(found);
        return staffRepository.findStaffById(staff.getId())
                .orElseThrow(() -> new StaffNotFoundException("Chỉnh sửa TK thất bại"));
    }

    @Override
    public void delete(Integer integer) {
        Staff staff = staffRepository.findById(integer).orElseThrow(() -> new StaffNotFoundException("Không thể xóa nhân viên này"));
        if(staff.isDeleted()){
            throw new RuntimeException("Nhân viên này đã bị xóa");
        }
        staff.setDeleted(true);
        staffRepository.save(staff);
    }

    @Override
    public void restore(Integer integer) {
        Staff staff = staffRepository.findById(integer).orElseThrow(() -> new StaffNotFoundException("Không thể phục hồi nhân viên này"));
        if (!staff.isDeleted()){
            throw new RuntimeException("không thể phục hồi tài khoản");
        }
        staff.setDeleted(false);
        staffRepository.save(staff);
    }

}
