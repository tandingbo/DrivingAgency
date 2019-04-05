package com.beautifulsoup.driving.tasks.scheduler;

import com.beautifulsoup.driving.tasks.job.AchievementJob;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
public class AchievementScheduler {

    public void scheduleAchievementJob(Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail= JobBuilder.newJob(AchievementJob.class).withIdentity("job1","achievementJob").build();
        CronScheduleBuilder scheduleBuilder=CronScheduleBuilder.cronSchedule("0 0 20 * * ?");
        CronTrigger cronTrigger= TriggerBuilder.newTrigger()
                .withIdentity("trigger1","achievementTrigger")
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail,cronTrigger);
    }
}
