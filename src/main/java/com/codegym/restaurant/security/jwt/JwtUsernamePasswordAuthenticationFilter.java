package com.codegym.restaurant.security.jwt;

import com.codegym.restaurant.dto.LoginSuccessResponseDTO;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            UsernamePasswordAuthenticationRequest authRequest = mapAuthenticationRequest(request);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            );
            return authenticationManager.authenticate(authToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UsernamePasswordAuthenticationRequest mapAuthenticationRequest(HttpServletRequest request) throws IOException {
        return new ObjectMapper().readValue(request.getInputStream(), UsernamePasswordAuthenticationRequest.class);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException {
        Staff staff = (Staff) authResult.getPrincipal();
        String accessToken = jwtUtils.buildAccessToken(staff);
        LoginSuccessResponseDTO responseDTO = new LoginSuccessResponseDTO("Bearer " + accessToken);
        String responseBody = new ObjectMapper().writeValueAsString(responseDTO);

        String refreshToken = jwtUtils.buildRefreshToken(staff);
        Cookie refreshCookie = new Cookie("RefreshToken", refreshToken);
        refreshCookie.setMaxAge(jwtUtils.getConfigExpirationDaysInSeconds());
        refreshCookie.setHttpOnly(true);

        response.addCookie(refreshCookie);
        response.setHeader("Set-Cookie", response.getHeader("Set-Cookie") + "; SameSite=strict");
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().write(responseBody);
    }
}