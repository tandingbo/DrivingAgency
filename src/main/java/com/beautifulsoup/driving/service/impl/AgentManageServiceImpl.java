package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.exception.ParamException;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.pojo.Role;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.repository.RoleRepository;
import com.beautifulsoup.driving.service.AgentManageService;
import com.beautifulsoup.driving.utils.MD5Util;
import com.beautifulsoup.driving.utils.ParamValidatorUtil;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class AgentManageServiceImpl implements AgentManageService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);
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

        Agent authentication=SecurityContextHolder.getAgent();
        if (authentication != null) {
            if (authentication.getParentId()==-1){
                agent.setStatus(1);
                agent.setParentId(0);
                Role role = roleRepository.findById(2).get();
                agent.setRole(role);
            }else if (authentication.getParentId()==0){
                agent.setStatus(0);
                agent.setParentId(1);
                Role role=roleRepository.findById(3).get();
                agent.setRole(role);
            }
            agentRepository.save(agent);
        }
        AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
        BeanUtils.copyProperties(agent,agentBaseInfoVo);
        return agentBaseInfoVo;
    }

}
