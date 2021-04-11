package com.codegym.restaurant.security.jwt;

import java.io.IOException;
import java.util.Collections;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codegym.restaurant.dto.AuthInfoDTO;
import com.codegym.restaurant.model.hr.RoleCode;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@AllArgsConstructor
public class JwtVerifierFilter extends OncePerRequestFilter {
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.replace(jwtConfig.getTokenPrefix(), "");

		try {
			tryAuthenticateJwtToken(token, request);
		}catch (SignatureException ex){
			throw new IllegalStateException("Invalid JWT Signature");
        }catch (MalformedJwtException ex){
        	throw new IllegalStateException("Invalid JWT Token");
        }catch (ExpiredJwtException ex){
        	throw new IllegalStateException("Expired JWT token");
        }catch (UnsupportedJwtException ex){
        	throw new IllegalStateException("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
        	throw new IllegalStateException("JWT claims string is empty");
        } catch (JwtException e) {
			throw new IllegalStateException("Token can not be verified");
		}

		filterChain.doFilter(request, response);
	}

	private void tryAuthenticateJwtToken(String token, HttpServletRequest request) {
		Claims claims = parseTokenClaims(token);
		UsernamePasswordAuthenticationToken authToken = buildAuthToken(claims);
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	
	private Claims parseTokenClaims(String token) {
		Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
		return claimsJws.getBody();
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
