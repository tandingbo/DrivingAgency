package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;

public interface AgentService {
    AgentBaseInfoVo login(String username, String password, HttpServletResponse response);
    AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result);
    AgentBaseInfoVo logout(String token);
    AgentBaseInfoVo resetPassword(String token, String username, String newPassword, String password, String email);
}
