package com.medicore.api.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "medicore.cors")
public record CorsProperties(String allowedOrigins) {
    public List<String> origins() {
        return Arrays.stream(allowedOrigins.split(",")).map(String::trim).filter(value -> !value.isBlank()).toList();
    }
}
