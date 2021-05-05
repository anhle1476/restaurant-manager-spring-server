package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.LoginSuccessResponseDTO;
import com.codegym.restaurant.exception.InvalidJwtException;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.service.StaffService;
import com.codegym.restaurant.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TokenController {
    @Autowired
    private StaffService staffService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/refresh")
    public ResponseEntity<?> doRefreshToken(@CookieValue("RefreshToken") String refreshCookie) {
        System.out.println(refreshCookie);
        Claims claims = jwtUtils.parseClaims(refreshCookie);
        String isRefreshToken = (String) claims.get("isRefreshToken");
        if (!isRefreshToken.equals("true"))
            throw new InvalidJwtException("Refesh token không hợp lệ");

        Integer staffId = Integer.parseInt((String) claims.get("staffId"));
        Staff refreshStaff = staffService.getById(staffId);
        String accessToken = jwtUtils.buildAccessToken(refreshStaff);

        return new ResponseEntity<>(new LoginSuccessResponseDTO("Bearer " + accessToken), HttpStatus.OK);
    }

    @GetMapping("/clear-cookie")
    public ResponseEntity<?> doLogout(HttpServletResponse response) {
        Cookie cookie = new Cookie("RefreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
