package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.service.AgentManageService;
import com.beautifulsoup.driving.utils.ParamValidatorUtil;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class AgentManageServiceImpl implements AgentManageService {
    @Override
    public AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);
        Agent agent=new Agent();
        BeanUtils.copyProperties(agentDto,agent);

        Agent authentication = SecurityContextHolder.getAgent();
        ParamValidatorUtil.validateContextHolderAgent(authentication);



        return null;
    }

}
