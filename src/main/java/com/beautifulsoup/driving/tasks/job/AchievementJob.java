package com.beautifulsoup.driving.tasks.job;

import com.beautifulsoup.driving.repository.AgentRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class AchievementJob implements Job {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
