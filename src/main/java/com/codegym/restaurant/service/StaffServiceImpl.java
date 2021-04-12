package com.codegym.restaurant.service;

import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.StaffRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl implements UserDetailsService, StaffService {

    @Autowired
    private StaffRepository staffRepository;

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
    public Staff getById(Integer integer) {
        return staffRepository.findAvailableById(integer)
                .orElseThrow(() -> new StaffNotFoundException("Không có tài khoản này"));
    }


    @Override
    public Staff create(Staff staff) {
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        return staffRepository.save(staff);
    }

    // cá độ huy anh xem pasword co null ko.
    @Override
    public Staff update(Staff staff) {
        Staff found = staffRepository.findById(staff.getId())
                .orElseThrow(() -> new StaffNotFoundException("Khong co"));
        found.setFullname(staff.getFullname());
        found.setPhoneNumber(staff.getPhoneNumber());
        found.setSalaryPerShift(staff.getSalaryPerShift());
        found.setRole(staff.getRole());
        return staffRepository.save(found);
    }

    @Override
    public void delete(Integer id) {
        Staff staff = getById(id);
        staff.setDeleted(true);
        staffRepository.save(staff);
    }

    @Override
    public void restore(Integer integer) {
        Staff staff = staffRepository.findById(integer).orElseThrow(() -> new StaffNotFoundException("Không thể phục hồi nhân viên này"));
        if (!staff.isDeleted())
            throw new RuntimeException("không thể phục hồi tài khoản");
        staff.setDeleted(false);
        staffRepository.save(staff);
    }

}
