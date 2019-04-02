package com.beautifulsoup.driving;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DrivingApplicationTests {

    @Autowired
    private AgentRepository agentRepository;

    @Test
    public void contextLoads() {
        AgentDto agentDto=AgentDto.builder().agentName("").build();
    }

}
