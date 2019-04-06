package com.beautifulsoup.driving.initial;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.pojo.Comment;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.repository.CommentRepository;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.beautifulsoup.driving.vo.AgentRankingVo;
import com.beautifulsoup.driving.vo.CommentVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

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

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public void run(String... args) throws Exception {
        log.info("=========Driving Agency 就绪===========");
        agentRepository.findAll().stream()
                .filter(agent -> agent.getParentId()>0)
                .forEach(agent -> {
//                    stringRedisTemplate.opsForHash().put(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,
//                            DrivingConstant.Redis.ACHIEVEMENT_AGENT+agent.getAgentName(),String.valueOf(agent.getAgentAchieve()));
                    AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
                    AgentRankingVo agentRankingVo=new AgentRankingVo();
                    BeanUtils.copyProperties(agent,agentRankingVo);
                    BeanUtils.copyProperties(agent,agentBaseInfoVo);
                    List<Comment> comments = commentRepository.findAllByName(agent.getAgentName());
                    if (CollectionUtils.isNotEmpty(comments)){
                        List<CommentVo> commentVos= Lists.newArrayList();
                        for (Comment comment:comments){
                            CommentVo commentVo=new CommentVo();
                            BeanUtils.copyProperties(comment,commentVo);
                            commentVos.add(commentVo);
                        }
                        agentRankingVo.setCommentVos(commentVos);
                    }

                    //维护其他信息,从缓存中获取,提高查询效率
                    redisTemplate.opsForHash().put(DrivingConstant.Redis.ACHIEVEMENT_AGENTS,
                            DrivingConstant.Redis.ACHIEVEMENT_AGENT+agent.getAgentName(),agentBaseInfoVo);

                    //维护排行榜信息
                    redisTemplate.opsForHash().put(DrivingConstant.Redis.RANKING_AGENTS,
                            DrivingConstant.Redis.RANKING_AGENT+agent.getAgentName(),agentRankingVo);
                });


    }
}
