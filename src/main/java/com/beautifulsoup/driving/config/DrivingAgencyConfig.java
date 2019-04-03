package com.beautifulsoup.driving.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@Import(value = {SwaggerConfig.class,CorsConfig.class})
public class DrivingAgencyConfig {
}
