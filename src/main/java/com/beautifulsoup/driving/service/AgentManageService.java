package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.beautifulsoup.driving.vo.AgentVo;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface AgentManageService {
    AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result);
    AgentBaseInfoVo examineExistsAgent(String username);
    List<AgentVo> listAllAgents();
    List<AgentVo> listAllUnExamineAgents();
}
