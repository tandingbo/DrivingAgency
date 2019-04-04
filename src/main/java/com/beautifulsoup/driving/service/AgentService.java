package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;

public interface AgentService {
    AgentBaseInfoVo adminLogin(String username, String password, HttpServletResponse response);
    AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result);
}
