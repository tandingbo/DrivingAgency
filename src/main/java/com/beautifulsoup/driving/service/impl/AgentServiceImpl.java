package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.AgentNewDto;
import com.beautifulsoup.driving.dto.UserTokenDto;
import com.beautifulsoup.driving.exception.AuthenticationException;
import com.beautifulsoup.driving.exception.ParamException;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.service.AgentService;
import com.beautifulsoup.driving.utils.*;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.beautifulsoup.driving.vo.AgentVo;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.TileObserver;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.beautifulsoup.driving.common.DrivingConstant.EMAIL_VALIDATE_CODE_PREFIX;

@Slf4j
@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public AgentBaseInfoVo login(String username, String password, HttpServletResponse response) {
        Preconditions.checkArgument(StringUtils.isNotBlank(username),"账户不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(password),"密码不能为空");

        Agent select = agentRepository.findAgentByAgentName(username);

        if (select == null) {
            throw new AuthenticationException("账户不存在,登陆失败");
        }

        String input= MD5Util.MD5Encode(password);
        if (!StringUtils.equals(input,select.getAgentPassword())){
            throw new AuthenticationException("密码错误,登录失败");
        }



        UserTokenDto userTokenDto=new UserTokenDto();
        BeanUtils.copyProperties(select,userTokenDto);
        String token = TokenUtil.conferToken(userTokenDto, DrivingConstant.TOKEN_EXPIRE);

        //保存用户状态，redis提高SQL查询性能
        select.setAgentPassword(null);
        redisTemplate.opsForHash().put(DrivingConstant.Redis.LOGIN_AGENTS,
                DrivingConstant.Redis.AGENT_TOKEN+token,select);
        stringRedisTemplate.opsForValue().set(DrivingConstant.Redis.TOKEN_REFRESH+token,UUID.randomUUID().toString());
        stringRedisTemplate.expire(DrivingConstant.Redis.TOKEN_REFRESH+token,DrivingConstant.REFRESH_TOKEN_EXPIRE,TimeUnit.SECONDS);
        response.setHeader("Cache-Control","no-store");
        response.setHeader("token",token);

        AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
        BeanUtils.copyProperties(select,agentBaseInfoVo);
        return agentBaseInfoVo;
    }


    @Override
    public AgentBaseInfoVo logout(String token) {
        Preconditions.checkArgument(StringUtils.isNotBlank(token),"token不能为空");
        try {
            Claims claims = TokenUtil.parseJWT(token);
            stringRedisTemplate.opsForHash().put(DrivingConstant.Redis.TOKEN_INVALID, token, DateTimeUtil.dateToMillis(new Date()));
            UserTokenDto userTokenDto= JsonSerializerUtil.string2Obj(claims.getSubject(),UserTokenDto.class);
            AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
            BeanUtils.copyProperties(userTokenDto,agentBaseInfoVo);
            return agentBaseInfoVo;
        }catch (Exception e){
            log.error("登出失败:{}",e);
        }
        return null;
    }

    @Override
    public AgentBaseInfoVo resetPassword(String token,String username, String newPassword, String password,String validateCode) {
        Preconditions.checkArgument(StringUtils.isNotBlank(token),"token不能为空");
        try {
            Claims claims = TokenUtil.parseJWT(token);
            UserTokenDto userTokenDto= JsonSerializerUtil.string2Obj(claims.getSubject(),UserTokenDto.class);
            if (!StringUtils.equals(userTokenDto.getAgentName(),username)){
                throw new ParamException("账户和token不一致,密码重置失败");
            }
            Agent dbAgent = agentRepository.findAgentByAgentName(username);
            String encodePassword = MD5Util.MD5Encode(password);
            if (!StringUtils.equals(encodePassword,dbAgent.getAgentPassword())){
                throw new ParamException("原密码输入错误");
            }
            if (StringUtils.isBlank(newPassword)||newPassword.length()>50){
                throw new ParamException("修改的密码长度不合适");
            }

            boolean validate = stringRedisTemplate.hasKey(EMAIL_VALIDATE_CODE_PREFIX + username).booleanValue();
            if (!validate){
                throw new ParamException("邮箱验证码已经失效");
            }
            String codeRaw=stringRedisTemplate.opsForValue().get(EMAIL_VALIDATE_CODE_PREFIX+username);
            if (!StringUtils.equals(codeRaw,validateCode)){
                throw new ParamException("邮箱验证码不正确");
            }

            String newEncodedPassword=MD5Util.MD5Encode(newPassword);
            dbAgent.setAgentPassword(newEncodedPassword);
            agentRepository.saveAndFlush(dbAgent);

            AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
            BeanUtils.copyProperties(userTokenDto,agentBaseInfoVo);
            stringRedisTemplate.opsForHash().put(DrivingConstant.Redis.TOKEN_INVALID,
                    token, DateTimeUtil.dateToMillis(new Date()));
            return agentBaseInfoVo;
        }catch (Exception e){
            log.error("密码重置失败:{}",e);
            throw new ParamException("密码重置失败");
        }
    }

    @Override
    public String sendEmail(String username,String email) {
        Agent dbAgent=agentRepository.findAgentByAgentName(username);

        if (dbAgent==null||!StringUtils.equals(email,dbAgent.getAgentEmail())){
                throw new ParamException("邮箱地址不正确,请输入您注册时的邮箱地址修改密码");
        }
        ImmutableList<Integer> immutableList = ImmutableList.of(RandomUtils.nextInt(1,10)
                ,RandomUtils.nextInt(1,10),RandomUtils.nextInt(1,10),RandomUtils.nextInt(1,10),RandomUtils.nextInt(1,10),
                RandomUtils.nextInt(1,10));
        String validateCode = immutableList.parallelStream().map(String::valueOf).collect(Collectors.joining(""));
        stringRedisTemplate.opsForValue().set(EMAIL_VALIDATE_CODE_PREFIX+dbAgent.getAgentName(),validateCode);
        stringRedisTemplate.expire(EMAIL_VALIDATE_CODE_PREFIX+dbAgent.getAgentName(),10, TimeUnit.MINUTES);
        mailSenderUtil.sendSimpleMail(email,"【驾校代理小程序验证码】",
                "亲，感谢您选择本驾校代理软件。您的本次验证码为: "+validateCode+"。"+"此验证码有效期10分钟,请您尽快处理。");
        return "邮件发送成功";
    }

    @Override
    public AgentBaseInfoVo updateAgentInfo(AgentNewDto agentNewDto,
                                           BindingResult result,String token) {
        ParamValidatorUtil.validateBindingResult(result);
        Agent dbAgent=SecurityContextHolder.getAgent();
        ParamValidatorUtil.validateContextHolderAgent(dbAgent);
        if (StringUtils.isNotBlank(agentNewDto.getAgentPhone())){
            dbAgent.setAgentPhone(agentNewDto.getAgentPhone());
        }
        if (StringUtils.isNotBlank(agentNewDto.getAgentEmail())){
            dbAgent.setAgentEmail(agentNewDto.getAgentEmail());
        }
        if (StringUtils.isNotBlank(agentNewDto.getAgentIdcardImg())){
            dbAgent.setAgentIdcardImg(dbAgent.getAgentIdcardImg());
        }
        if (StringUtils.isNotBlank(agentNewDto.getAgentSchool())){
            dbAgent.setAgentSchool(agentNewDto.getAgentSchool());
        }
        agentRepository.saveAndFlush(dbAgent);
        redisTemplate.opsForHash().put(DrivingConstant.Redis.LOGIN_AGENTS,
                DrivingConstant.Redis.AGENT_TOKEN+token,dbAgent);
        AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
        BeanUtils.copyProperties(dbAgent,agentBaseInfoVo);
        return agentBaseInfoVo;
    }

    @Override
    public Agent getAgentInfo() {
        Agent agent = SecurityContextHolder.getAgent();
        ParamValidatorUtil.validateContextHolderAgent(agent);
        return agent;
    }

    @Override
    public AgentVo retrievePassword(String username,String password,String validateCode) {
        String codeRaw=stringRedisTemplate.opsForValue().get(EMAIL_VALIDATE_CODE_PREFIX+username);
        if (!StringUtils.equals(codeRaw,validateCode)){
            throw new ParamException("邮箱验证码不正确");
        }
        Agent agent=agentRepository.findAgentByAgentName(username);
        if (agent!=null){
            agent.setAgentPassword(MD5Util.MD5Encode(password));
            agentRepository.saveAndFlush(agent);
            AgentVo agentVo=new AgentVo();
            BeanUtils.copyProperties(agent,agentVo);
            return agentVo;
        }
        return null;
    }


}
