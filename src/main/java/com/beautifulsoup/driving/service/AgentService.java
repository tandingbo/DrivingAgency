package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import org.springframework.validation.BindingResult;

public interface AgentService {
    AgentBaseInfoVo adminLogin(String username,String password);
    AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result);
}
