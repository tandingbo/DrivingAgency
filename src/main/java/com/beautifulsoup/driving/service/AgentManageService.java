package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import org.springframework.validation.BindingResult;

public interface AgentManageService {
    AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result);
}
