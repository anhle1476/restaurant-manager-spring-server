package com.codegym.restaurant.dto;

import com.codegym.restaurant.model.hr.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
public class StaffCreationDTO {
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,25}$", message = "Tên người dùng phải chứa từ 4-25 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Tên người dùng không được trống")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9@#$%^&+=()_-]{8,20}$", message = "Mật khẩu phải từ 8-20 ký tự, bao gồm chữ, số và các ký tự @#$%^&+=()_-")
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @Pattern(regexp = "^[\\pL ]{4,50}$", message = "Tên người dùng phải chứa từ 4-50 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Họ và tên nhân viên không được trống")
    private String fullname;

    @Pattern(regexp = "^0[\\d]{9,10}$", message = "Số điện thoại phải chứa 10-11 số và bắt đầu bằng số 0")
    @NotBlank(message = "Số điện thoại không được trống")
    private String phoneNumber;

    @Positive(message = "Lương mỗi ca không được âm")
    private long salaryPerShift;

    @NotNull(message = "Chức vụ không được để trống")
    private Role role;
}
