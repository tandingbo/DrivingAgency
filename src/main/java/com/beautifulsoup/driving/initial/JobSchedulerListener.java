package com.beautifulsoup.driving.initial;

import com.beautifulsoup.driving.tasks.AchievementSchedulerJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class JobSchedulerListener implements CommandLineRunner {

    @Autowired
    private AchievementSchedulerJob achievementSchedulerJob;

    @Override
    public void run(String... args) throws Exception {
        achievementSchedulerJob.scheduleJobs();
    }
}
