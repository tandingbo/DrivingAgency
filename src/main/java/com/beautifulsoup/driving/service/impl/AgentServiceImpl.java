package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.UserTokenDto;
import com.beautifulsoup.driving.exception.AuthenticationException;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.service.AgentService;
import com.beautifulsoup.driving.utils.MD5Util;
import com.beautifulsoup.driving.utils.ParamValidatorUtil;
import com.beautifulsoup.driving.utils.TokenUtil;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public AgentBaseInfoVo adminLogin(String username, String password) {
        Preconditions.checkArgument(StringUtils.isNotBlank(username),"账户不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(password),"密码不能为空");

        Agent select = agentRepository.findAgentByAgentNameAndParentId(username,-1);

        if (select == null) {
            throw new AuthenticationException("账户不存在,管理员登陆失败");
        }

        String input= MD5Util.MD5Encode(password);
        if (!StringUtils.equals(input,password)){
            throw new AuthenticationException("密码错误,管理员登录失败");
        }

        //账户登陆成功
        UserTokenDto userTokenDto=new UserTokenDto();
        BeanUtils.copyProperties(select,userTokenDto);
        String token = TokenUtil.conferToken(userTokenDto, DrivingConstant.TOKEN_EXPIRE);

        //保存用户状态
        stringRedisTemplate.opsForValue().set(DrivingConstant.Redis.ADMIN_TOKEN+token,select.getAgentName());
        stringRedisTemplate.expire(DrivingConstant.Redis.ADMIN_TOKEN+token,DrivingConstant.TOKEN_EXPIRE, TimeUnit.MILLISECONDS);
        redisTemplate.opsForHash().put(DrivingConstant.Redis.LOGIN_AGENTS,
                DrivingConstant.Redis.ADMIN_TOKEN+token,select);
        AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();

        agentBaseInfoVo.setToken(token);

        return agentBaseInfoVo;
    }

    @Override
    public AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);
        Agent agent=new Agent();
        BeanUtils.copyProperties(agentDto,agent);

        Agent authentication = SecurityContextHolder.getAgent();

//        authentication.getRole().


        return null;
    }





}
