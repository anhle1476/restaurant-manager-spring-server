package com.codegym.restaurant.uploader;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "application.uploader")
@Data
public class CloudinaryConfig {
    @Value("${application.uploader.cloud-name}")
    private String cloudName;
    @Value("application.uploader.api-key")
    private String apiKey;
    @Value("application.uploader.api-secret")
    private String apiSecret;

}
