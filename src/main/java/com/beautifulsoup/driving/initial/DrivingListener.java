package com.beautifulsoup.driving.initial;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(value = 1)
@Component
public class DrivingListener implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("=========Driving Agency 就绪===========");
    }
}
