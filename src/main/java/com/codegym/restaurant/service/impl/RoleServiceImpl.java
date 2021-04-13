package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.RoleNotFoundException;
import com.codegym.restaurant.model.hr.Role;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.RoleRepository;
import com.codegym.restaurant.repository.StaffRepository;
import com.codegym.restaurant.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAllAvailable();
    }

    @Override
    public List<Role> getAllDeleted() {
        return roleRepository.findAllDeleted();
    }

    @Override
    public Role getById(Integer integer) {
        return roleRepository.findAvailableById(integer)
                .orElseThrow(() -> new RoleNotFoundException("Không tìm thấy Role"));
    }

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void delete(Integer integer) {
        Role role = getById(integer);
        role.setDeleted(true);
        roleRepository.save(role);
    }

    @Override
    public void restore(Integer integer) {
        Role role = roleRepository.findById(integer)
                .orElseThrow(() -> new RoleNotFoundException("Role Này không tồn tại"));
        if(!role.isDeleted())
            throw new EntityRestoreFailedException("Không thể phục hồi khi đối tượng chưa xóa");
        role.setDeleted(false);
        roleRepository.save(role);
    }

    @Override
    public List<Staff> getAllStaffsOfRole(Integer id) {
        return staffRepository.findStaffByRoleId(id);
    }
}
