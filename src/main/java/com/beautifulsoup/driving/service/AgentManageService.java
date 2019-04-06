package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.AnnouncementDto;
import com.beautifulsoup.driving.dto.CommentDto;
import com.beautifulsoup.driving.vo.*;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface AgentManageService {
    AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result);
    AnnouncementVo publishAnnouncement(AnnouncementDto announcementDto, BindingResult result);
    AgentBaseInfoVo examineExistsAgent(String username);
    List<AgentVo> listAllProcessedAgents();
    List<AgentVo> listAllUnExamineAgents();
    AnnouncementVo getLatestAnnouncement();
    List<AgentBaseInfoVo> listAllAgentsByDailyAchievements();
    List<AgentBaseInfoVo> listAllAgentsByTotalAchievements();
    List<AgentBaseInfoVo> listChildrenAgentsByName(String username);
    List<AgentVo> listAllAgents();
    List<AgentRankingVo> rankingListbyDailyAchievements();
    List<AgentRankingVo> rankingListbyTotalAchievements();
    AgentRankingVo starAgent(String username);
    AgentRankingVo publishCommentByAgentName(CommentDto commentDto, BindingResult result);
    List<CommentVo> rankingCommentsListByName(String username);
}
