package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.pojo.Role;
import com.beautifulsoup.driving.utils.MD5Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AgentRepositoryTest {
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Test
    public void saveAgentTest() {
        AgentDto agentDto=AgentDto.builder()
                .agentName("Admin")
//                .agentPassword(MD5Util.MD5Encode("123456"))
                .agentEmail("beautifulsoup@163.com")
                .agentPhone("17864195200")
//                .status(1)//状态正常,可用
//                .parentId(-1)//1级代理
                .agentIdcard("372330000007777663220")
                .agentSchool("山东师范大学")
                .agentIdcardImg("http://39.106.62.161:8888/driving/M00/00/00/111")
                .build();
        Agent agent=new Agent();
        BeanUtils.copyProperties(agentDto,agent);

        Role role=roleRepository.findById(2).get();
        agent.setRole(role);
        agentRepository.saveAndFlush(agent);
    }

    //授权操作
    @Test
    public void updateAgentTest(){
        Agent agent=agentRepository.findById(1).get();
        Role role=roleRepository.findById(1).get();
        agent.setRole(role);
        agentRepository.saveAndFlush(agent);
    }

    @Test
    public void findAllAgentInfo(){
        List<Agent> all = agentRepository.findAll();
        log.info(Arrays.toString(all.toArray()));
    }



}
