package com.codegym.restaurant.utils;

import com.codegym.restaurant.exception.InvalidJwtException;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.security.jwt.JwtConfig;
import com.codegym.restaurant.security.jwt.JwtSecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private JwtSecretKey jwtSecretKey;
    @Autowired
    private DateUtils dateUtils;

    private String buildToken(Map<String, String> claims, Date expiredDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(jwtSecretKey.secretKey())
                .compact();
    }

    public Map<String, String> buildAccessTokenClaims(Staff staff) {
        Map<String, String> claims = new HashMap<>();
        claims.put("staffId", String.valueOf(staff.getId()));
        claims.put("username", String.valueOf(staff.getUsername()));
        claims.put("role", staff.getRole().getCode().name());
        return claims;
    }

    public Map<String, String> buildRefreshTokenClaims(Staff staff) {
        Map<String, String> refreshTokenClaims = buildAccessTokenClaims(staff);
        refreshTokenClaims.put("isRefreshToken", "true");
        return refreshTokenClaims;
    }

    public String buildAccessToken(Staff staff) {
        Map<String, String> claims = buildAccessTokenClaims(staff);
        return buildToken(claims, dateUtils.dateFromNow(10, ChronoUnit.MINUTES));
    }

    public String buildRefreshToken(Staff staff) {
        Map<String, String> claims = buildRefreshTokenClaims(staff);
        return buildToken(claims, dateUtils.dateFromNow(jwtConfig.getTokenExpirationAfterDays(), ChronoUnit.DAYS));
    }

    public Integer getConfigExpirationDaysInSeconds() {
        return jwtConfig.getTokenExpirationAfterDays() * 24 * 60 * 60;
    }

    public Claims parseClaims(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey.secretKey())
                    .build()
                    .parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (SignatureException
                | MalformedJwtException
                | UnsupportedJwtException
                | IllegalArgumentException ex) {
            throw new InvalidJwtException("JWT token không hợp lệ");
        } catch (ExpiredJwtException ex) {
            throw new InvalidJwtException("Phiên đăng nhập hết hạn");
        } catch (JwtException e) {
            throw new InvalidJwtException("Không thể xác minh JWT token");
        }

    }
}
