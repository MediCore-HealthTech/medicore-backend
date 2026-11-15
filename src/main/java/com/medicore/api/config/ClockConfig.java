package com.medicore.api.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {
    public static final ZoneId LIMA_ZONE = ZoneId.of("America/Lima");
    @Bean
    Clock clock() { return Clock.system(LIMA_ZONE); }
}
