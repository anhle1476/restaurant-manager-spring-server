package com.codegym.restaurant.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateStaffPasswordDTO {
    @NotNull
    private Integer staffId;

    @Pattern(regexp = "^[a-zA-Z0-9@#$%^&+=()_-]{8,20}$", message = "Mật khẩu phải từ 8-20 ký tự, bao gồm chữ, số và các ký tự @#$%^&+=()_-")
    @NotBlank(message = "Mật khẩu không được để trống")
    private String newPassword;

}
