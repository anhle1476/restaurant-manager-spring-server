package com.codegym.restaurant.dto;

import com.codegym.restaurant.model.hr.Role;

public interface StaffInfoDTO {

    Integer getId();

    String getUsername();

    String getFullname();

    String getPhoneNumber();

    long getSalaryPerShift();

    Role getRole();

    boolean isDeleted();
}
