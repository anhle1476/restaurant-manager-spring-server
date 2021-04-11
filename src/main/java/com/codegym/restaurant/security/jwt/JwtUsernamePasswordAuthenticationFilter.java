package com.codegym.restaurant.security.jwt;

import com.codegym.restaurant.dto.LoginSuccessResponseDTO;
import com.codegym.restaurant.model.hr.Staff;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

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
        String token = buildJwtToken(authResult);
        LoginSuccessResponseDTO responseDTO = new LoginSuccessResponseDTO(jwtConfig.getTokenPrefix() + token);
        String responseBody = new ObjectMapper().writeValueAsString(responseDTO);

        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().write(responseBody);
    }

    private String buildJwtToken(Authentication authResult) {
        Staff staff = (Staff) authResult.getPrincipal();
        Map<String, String> claims = new HashMap<>();
        claims.put("staffId", String.valueOf(staff.getId()));
        claims.put("username", String.valueOf(staff.getUsername()));
        claims.put("role", staff.getRole().getCode().name());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
    }
}