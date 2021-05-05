package com.codegym.restaurant.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.jwt")
@Data
public class JwtConfig {

    private String secretKey;
    private String corsPattern;
    private Integer tokenExpirationAfterDays;

}

