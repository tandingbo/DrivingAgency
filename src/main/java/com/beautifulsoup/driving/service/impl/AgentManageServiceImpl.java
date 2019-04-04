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
import com.beautifulsoup.driving.vo.AgentVo;
import com.beautifulsoup.driving.vo.RoleVo;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            if (authentication.getParentId()==0){
                agent.setStatus(1);
                agent.setParentId(1);
                Role role = roleRepository.findById(2).get();
                agent.setRole(role);
            }else{
                agent.setStatus(0);
                agent.setParentId(authentication.getId());
                Role role=roleRepository.findById(3).get();
                agent.setRole(role);
            }
            agentRepository.save(agent);
        }
        AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
        BeanUtils.copyProperties(agent,agentBaseInfoVo);
        return agentBaseInfoVo;
    }

    @Override
    public AgentBaseInfoVo examineExistsAgent(String username) {
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
