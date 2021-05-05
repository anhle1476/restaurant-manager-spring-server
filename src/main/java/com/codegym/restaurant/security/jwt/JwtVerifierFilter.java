package com.codegym.restaurant.security.jwt;

import com.codegym.restaurant.dto.AuthInfoDTO;
import com.codegym.restaurant.model.hr.RoleCode;
import com.codegym.restaurant.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
public class JwtVerifierFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.replace("Bearer ", "");
			authenticateJwtToken(token, request);
		}

		filterChain.doFilter(request, response);
	}

	private void authenticateJwtToken(String token, HttpServletRequest request) {
		Claims claims = jwtUtils.parseClaims(token);
		UsernamePasswordAuthenticationToken authToken = buildAuthToken(claims);
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

	private UsernamePasswordAuthenticationToken buildAuthToken(Claims claims) {
		Integer id = Integer.parseInt((String) claims.get("staffId"));
		String username = (String) claims.get("username");
		String role = (String) claims.get("role");
		RoleCode roleCode = RoleCode.valueOf(role);
		AuthInfoDTO dto = new AuthInfoDTO(id, username, roleCode);

		return new UsernamePasswordAuthenticationToken(
				dto,
				null,
				Collections.singletonList(new SimpleGrantedAuthority(role))
		);
	}
}
