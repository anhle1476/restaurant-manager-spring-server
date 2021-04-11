package com.codegym.restaurant.dto;

import com.codegym.restaurant.model.hr.RoleCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthInfoDTO {
    private Integer id;
    private String username;
    private RoleCode roleCode;
}
