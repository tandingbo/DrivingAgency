package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.AgentNewDto;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.beautifulsoup.driving.vo.AgentVo;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;

public interface AgentService {
    AgentBaseInfoVo login(String username, String password, HttpServletResponse response);

    AgentBaseInfoVo logout(String token);
    AgentBaseInfoVo resetPassword(String token, String username, String newPassword, String password,String validateCode);
    String sendEmail(String username,String email);
    AgentBaseInfoVo updateAgentInfo(AgentNewDto agentNewDto, BindingResult result,String token);
    Agent getAgentInfo();
    AgentVo retrievePassword(String username, String password, String validateCode);
}
