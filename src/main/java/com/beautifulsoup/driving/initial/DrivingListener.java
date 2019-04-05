package com.beautifulsoup.driving.initial;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Order(value = 1)
@Component
public class DrivingListener implements CommandLineRunner {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("=========Driving Agency 就绪===========");
        agentRepository.findAll().stream()
                .filter(agent -> agent.getParentId()>0)
                .forEach(agent -> {
                    stringRedisTemplate.opsForHash().put(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,
                            DrivingConstant.Redis.ACHIEVEMENT_AGENT+agent.getAgentName(),String.valueOf(agent.getAgentAchieve()));
                });

    }
}
