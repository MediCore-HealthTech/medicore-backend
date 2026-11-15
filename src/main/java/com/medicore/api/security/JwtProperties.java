package com.medicore.api.security;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "medicore.security")
public record JwtProperties(@NotBlank String jwtSecret, @Min(1) long jwtExpirationMinutes) {
    public boolean hasMinimumSecretLength() { return jwtSecret != null && jwtSecret.getBytes(StandardCharsets.UTF_8).length >= 32; }
}
