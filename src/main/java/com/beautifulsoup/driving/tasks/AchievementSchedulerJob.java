package com.beautifulsoup.driving.tasks;

import com.beautifulsoup.driving.tasks.scheduler.AchievementScheduler;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class AchievementSchedulerJob {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private AchievementScheduler achievementScheduler;

    public void scheduleJobs() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        achievementScheduler.scheduleAchievementJob(scheduler);
    }

}
