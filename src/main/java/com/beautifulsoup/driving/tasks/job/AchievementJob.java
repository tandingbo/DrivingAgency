package com.beautifulsoup.driving.tasks.job;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.google.common.base.Splitter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class AchievementJob implements Job {

    private static final Splitter splitter=Splitter.on(":").trimResults().omitEmptyStrings();

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        stringRedisTemplate.opsForHash().entries(DrivingConstant.Redis.ACHIEVEMENT_TOTAL)
                .keySet().forEach(key->{
            String agentName = splitter.splitToList((CharSequence) key).get(1);
            Agent agentByAgentName = agentRepository.findAgentByAgentName(agentName);
            if (agentByAgentName != null) {
                String achieveTotal = (String) stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_TOTAL, key);
                agentByAgentName.setAgentAchieve(Integer.parseInt(achieveTotal));
                agentRepository.save(agentByAgentName);
            }
        });
    }
}
