package com.beautifulsoup.driving.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {SwaggerConfig.class})
public class DrivingAgencyConfig {
}
