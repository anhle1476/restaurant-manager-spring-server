package com.codegym.restaurant.service;

import com.codegym.restaurant.model.hr.Role;
import com.codegym.restaurant.model.hr.Staff;

import java.util.List;

public interface RoleService extends BaseService<Role,Integer> {
    List<Staff> getAllStaffsOfRole(Integer id);
}
