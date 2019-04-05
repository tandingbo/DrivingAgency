package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.AnnouncementDto;
import com.beautifulsoup.driving.enums.AgentStatus;
import com.beautifulsoup.driving.enums.RoleCode;
import com.beautifulsoup.driving.exception.AuthenticationException;
import com.beautifulsoup.driving.exception.ParamException;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.pojo.Announcement;
import com.beautifulsoup.driving.pojo.Role;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.repository.AnnouncementRepository;
import com.beautifulsoup.driving.repository.RoleRepository;
import com.beautifulsoup.driving.service.AgentManageService;
import com.beautifulsoup.driving.utils.MD5Util;
import com.beautifulsoup.driving.utils.ParamValidatorUtil;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.beautifulsoup.driving.vo.AgentVo;
import com.beautifulsoup.driving.vo.AnnouncementVo;
import com.beautifulsoup.driving.vo.RoleVo;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgentManageServiceImpl implements AgentManageService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    public AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);
        Agent authentication=SecurityContextHolder.getAgent();
        if (authentication.getStatus().equals(AgentStatus.UNEXAMINED.getCode())){
            throw new AuthenticationException("对不起,你还没通过超管审核,还不能添加代理");
        }
        Agent agent=new Agent();
        BeanUtils.copyProperties(agentDto,agent);
        agent.setAgentIdcardImg(MoreObjects.firstNonNull(Strings.emptyToNull(agent.getAgentIdcardImg()),"http://www.aa.jpg"));
        agent.setAgentSchool(MoreObjects.firstNonNull(Strings.emptyToNull(agent.getAgentSchool()),"sdnu"));
        agent.setAgentAchieve(0);
        agent.setAgentPassword(MD5Util.MD5Encode("000000"));

        Agent agentByAgentName = agentRepository.findAgentByAgentName(agentDto.getAgentName());
        if (agentByAgentName != null) {
            throw new ParamException("代理的名字已经存在,添加失败");
        }


        if (authentication != null) {
            if (authentication.getParentId().equals(RoleCode.ROLE_ADMIN.getType())){
                agent.setStatus(AgentStatus.EXAMINED.getCode());
                agent.setParentId(RoleCode.ROLE_FIRST_TIER_AGENT.getType());
                
                Role role = roleRepository.findById(2).get();
                agent.setRole(role);
            }else{
                agent.setStatus(AgentStatus.UNEXAMINED.getCode());
                agent.setParentId(authentication.getId());
                //总业绩和当天业绩的更新
                stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_DAILY,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),1);
                stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),1);

                stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_TOTAL_ORDER,
                        DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),Double.parseDouble(Strings.nullToEmpty((String) stringRedisTemplate.opsForHash()
                                .get(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()))));
                stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_DAILY_ORDER,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()
                ,Double.parseDouble(Strings.nullToEmpty((String) stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_DAILY,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()))));
                Role role=roleRepository.findById(3).get();
                agent.setRole(role);
                agentRepository.save(authentication);
            }
            agentRepository.save(agent);
        }
        AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
        BeanUtils.copyProperties(agent,agentBaseInfoVo);
        return agentBaseInfoVo;
    }

    @Override
    public AnnouncementVo publishAnnouncement(AnnouncementDto announcementDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);
        Announcement announcement=new Announcement();
        BeanUtils.copyProperties(announcementDto,announcement);
        announcement.setPublishTime(new Date());
        announcementRepository.save(announcement);

        AnnouncementVo announcementVo=new AnnouncementVo();
        BeanUtils.copyProperties(announcement,announcementVo);
        return announcementVo;
    }

    @Override
    public AgentBaseInfoVo examineExistsAgent(String username) {
        Agent agent=agentRepository.findAgentByAgentName(username);
        if (agent != null) {
            if (agent.getStatus().equals(AgentStatus.UNEXAMINED.getCode())){
                agent.setStatus(AgentStatus.EXAMINED.getCode());
                agentRepository.save(agent);
            }
            AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
            BeanUtils.copyProperties(agent,agentBaseInfoVo);
            return agentBaseInfoVo;
        }

        return null;
    }

    @Override
    public List<AgentVo> listAllAgents() {
        Agent agent = SecurityContextHolder.getAgent();
        ParamValidatorUtil.validateContextHolderAgent(agent);
        Set<Agent> agents= Sets.newConcurrentHashSet();
        findChildrenAgents(agents,agent.getId());
        List<AgentVo> lists= Lists.newArrayList();

        agents.stream().sorted(Comparator.comparing(Agent::getAgentAchieve).reversed()).forEach(agent1->{
            AgentVo agentVo=new AgentVo();
            RoleVo roleVo=new RoleVo();
            BeanUtils.copyProperties(agent1,agentVo);
            BeanUtils.copyProperties(agent1.getRole(),roleVo);
            agentVo.setRoleVo(roleVo);
            lists.add(agentVo);
        });
        return lists;
    }

    @Override
    public List<AgentVo> listAllUnExamineAgents() {
        List<AgentVo> collect = listAllAgents().stream().filter(agentVo -> agentVo.getStatus() == 0).collect(Collectors.toList());
        return collect;
    }

    @Override
    public AnnouncementVo getLatestAnnouncement() {
        Announcement announcement = announcementRepository.findFirstByOrderByPublishTimeDesc();
        if (announcement != null) {
            AnnouncementVo announcementVo=new AnnouncementVo();
            BeanUtils.copyProperties(announcement,announcementVo);
            return announcementVo;
        }
        return null;
    }

    private Set<Agent> findChildrenAgents(Set<Agent> agents,Integer parentId){
        Optional<Agent> optional = agentRepository.findById(parentId);
        if (optional.isPresent()){
            agents.add(optional.get());
        }
        List<Agent> allByParentId = agentRepository.findAllByParentId(parentId);
        for (Agent agent:allByParentId){
            findChildrenAgents(agents,agent.getId());
        }
        return agents;
    }

}
