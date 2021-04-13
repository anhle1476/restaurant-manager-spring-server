package com.codegym.restaurant.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdateAccountInfoDTO {
    @Pattern(regexp = "^[\\pL ]{4,50}$", message = "Tên người dùng phải chứa từ 4-50 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Họ và tên nhân viên không được trống")
    private String fullname;

    @Pattern(regexp = "^0[\\d]{9,10}$", message = "Số điện thoại phải chứa 10-11 số và bắt đầu bằng số 0")
    @NotBlank(message = "Số điện thoại không được trống")
    private String phoneNumber;
}
