package com.codegym.restaurant.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdateAccountPasswordDTO {
    @Pattern(regexp = "^[a-zA-Z0-9@#$%^&+=()_-]{8,20}$", message = "Mật khẩu phải từ 8-20 ký tự, bao gồm chữ, số và các ký tự @#$%^&+=()_-")
    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String currentPassword;

    @Pattern(regexp = "^[a-zA-Z0-9@#$%^&+=()_-]{8,20}$", message = "Mật khẩu phải từ 8-20 ký tự, bao gồm chữ, số và các ký tự @#$%^&+=()_-")
    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;
}
