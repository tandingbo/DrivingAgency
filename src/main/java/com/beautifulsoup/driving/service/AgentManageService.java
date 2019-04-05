package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.AnnouncementDto;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.beautifulsoup.driving.vo.AgentVo;
import com.beautifulsoup.driving.vo.AnnouncementVo;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface AgentManageService {
    AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result);
    AnnouncementVo publishAnnouncement(AnnouncementDto announcementDto, BindingResult result);
    AgentBaseInfoVo examineExistsAgent(String username);
    List<AgentVo> listAllAgents();
    List<AgentVo> listAllUnExamineAgents();
    AnnouncementVo getLatestAnnouncement();
    List<AgentBaseInfoVo> listAllAgentsByDailyAchievements();
    List<AgentBaseInfoVo> listAllAgentsByTotalAchievements();
    List<AgentBaseInfoVo> listChildrenAgentsByName(String username);
}
