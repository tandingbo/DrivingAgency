package com.beautifulsoup.driving.initial;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Order(value = 1)
@Component
public class DrivingListener implements CommandLineRunner {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Override
    public void run(String... args) throws Exception {
        log.info("=========Driving Agency 就绪===========");
        agentRepository.findAll().stream()
                .filter(agent -> agent.getParentId()>0)
                .forEach(agent -> {
//                    stringRedisTemplate.opsForHash().put(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,
//                            DrivingConstant.Redis.ACHIEVEMENT_AGENT+agent.getAgentName(),String.valueOf(agent.getAgentAchieve()));
                    AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
                    BeanUtils.copyProperties(agent,agentBaseInfoVo);
                    redisTemplate.opsForHash().put(DrivingConstant.Redis.ACHIEVEMENT_AGENTS,
                            DrivingConstant.Redis.ACHIEVEMENT_AGENT+agent.getAgentName(),agentBaseInfoVo);
                });


    }
}
