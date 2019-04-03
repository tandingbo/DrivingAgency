package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.dto.UserTokenDto;
import com.beautifulsoup.driving.exception.AuthenticationException;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.service.AgentService;
import com.beautifulsoup.driving.utils.MD5Util;
import com.beautifulsoup.driving.utils.TokenUtil;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public AgentBaseInfoVo adminLogin(String username, String password) {
        Preconditions.checkArgument(StringUtils.isNotBlank(username),"用户名不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(password),"密码不能为空");

        Agent select = agentRepository.findAgentByAgentName(username);

        if (select == null) {
            throw new AuthenticationException("代理不存在,登陆失败");
        }

        String input= MD5Util.MD5Encode(password);
        if (!StringUtils.equals(input,password)){
            throw new AuthenticationException("密码错误,用户登录失败");
        }

        UserTokenDto userTokenDto=new UserTokenDto();
        BeanUtils.copyProperties(select,userTokenDto);
        String token = TokenUtil.conferToken(userTokenDto, DrivingConstant.TOKEN_EXPIRE);


        return null;
    }

}
