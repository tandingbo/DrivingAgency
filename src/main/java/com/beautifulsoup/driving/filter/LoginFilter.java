package com.beautifulsoup.driving.filter;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.UserTokenDto;
import com.beautifulsoup.driving.exception.AuthenticationException;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.utils.JsonSerializerUtil;
import com.beautifulsoup.driving.utils.ResponseUtil;
import com.beautifulsoup.driving.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Order(value = 1)
@WebFilter(value = {"/agent/add", "/agent/sendmail","/agent/update","/agent/get"},
        filterName = "loginFilter")
public class LoginFilter implements Filter {



    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String,Serializable> redisTemplate;

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest= (HttpServletRequest) request;
        HttpServletResponse servletResponse= (HttpServletResponse) response;
        String token = servletRequest.getHeader("token");
        if (StringUtils.isBlank(token)){
            ResponseUtil.errorAuthentication(servletResponse,"用户未登录,请登录后访问");
            return;
        }
        //检查黑名单
        Boolean token_status = stringRedisTemplate.opsForHash().hasKey(DrivingConstant.Redis.TOKEN_INVALID, token);
        if (token_status.booleanValue()){
            ResponseUtil.warningAuthentication(servletResponse,"用户登录状态已失效,请重新登陆");
            return;
        }
        try {
            Claims claims = TokenUtil.parseJWT(token);
            String subject = claims.getSubject();
            log.info(subject);
            UserTokenDto userTokenDto= JsonSerializerUtil.string2Obj(subject,UserTokenDto.class);
            Boolean hasAdmin = redisTemplate.opsForHash().hasKey(DrivingConstant.Redis.LOGIN_AGENTS,
                        DrivingConstant.Redis.AGENT_TOKEN + token);
            if (!hasAdmin.booleanValue()) {
                Agent agent1 = agentRepository.findAgentByAgentName(userTokenDto.getAgentName());
                if (agent1==null){
                    ResponseUtil.errorAuthentication(servletResponse,"账户异常");
                    return;
                }
                agent1.setAgentPassword(null);
                redisTemplate.opsForHash().put(DrivingConstant.Redis.LOGIN_AGENTS,
                            DrivingConstant.Redis.AGENT_TOKEN+token,agent1);
            }
            Agent agent=(Agent) redisTemplate.opsForHash().get(DrivingConstant.Redis.LOGIN_AGENTS,
                        DrivingConstant.Redis.AGENT_TOKEN+token);
            SecurityContextHolder.addAgent(agent);
            chain.doFilter(servletRequest,servletResponse);
            return;
        }catch (ExpiredJwtException e){
            log.error("Token已失效");
            Agent agent=(Agent) redisTemplate.opsForHash().get(DrivingConstant.Redis.LOGIN_AGENTS,
                    DrivingConstant.Redis.AGENT_TOKEN+token);
            boolean isValid = stringRedisTemplate.hasKey(DrivingConstant.Redis.TOKEN_REFRESH + token).booleanValue();
            if (isValid&&null!=agent){
                //refresh token  仍然有效
                Boolean hasKey = stringRedisTemplate.hasKey(DrivingConstant.Redis.TOKEN_BLACKLIST + token);
                if (hasKey.booleanValue()){
                    //解决并发环境下问题
                    return;
                }

                UserTokenDto userTokenDto=new UserTokenDto();
                BeanUtils.copyProperties(agent,userTokenDto);
                String newToken = TokenUtil.conferToken(userTokenDto, DrivingConstant.TOKEN_EXPIRE);
                HttpServletResponse httpServletResponse=(HttpServletResponse) response;
                HttpServletRequest httpServletRequest=(HttpServletRequest)request;
                httpServletResponse.setHeader("Cache-Control","no-store");
                httpServletResponse.setHeader("token",newToken);
                Long oldExpire = stringRedisTemplate.getExpire(DrivingConstant.Redis.TOKEN_REFRESH + token, TimeUnit.SECONDS);
                stringRedisTemplate.opsForValue().set(DrivingConstant.Redis.TOKEN_REFRESH+newToken,UUID.randomUUID().toString());
                stringRedisTemplate.expire(DrivingConstant.Redis.TOKEN_REFRESH+newToken,oldExpire,TimeUnit.SECONDS);
                stringRedisTemplate.delete(DrivingConstant.Redis.TOKEN_REFRESH + token);
                //旧Token加入黑名单,解决并发下的问题
                stringRedisTemplate.opsForValue().set(DrivingConstant.Redis.TOKEN_BLACKLIST + token,newToken);
                stringRedisTemplate.expire(DrivingConstant.Redis.TOKEN_BLACKLIST + token,30,TimeUnit.SECONDS);

                redisTemplate.opsForHash().delete(DrivingConstant.Redis.LOGIN_AGENTS,
                        DrivingConstant.Redis.AGENT_TOKEN+token);
                //继续当前请求,状态仍然需要维护
                Agent agent1 = agentRepository.findAgentByAgentName(userTokenDto.getAgentName());
                redisTemplate.opsForHash().put(DrivingConstant.Redis.LOGIN_AGENTS,
                         DrivingConstant.Redis.AGENT_TOKEN+newToken,agent1);
                SecurityContextHolder.addAgent(agent1);
                chain.doFilter(httpServletRequest,httpServletResponse);
                return;
            }
            ResponseUtil.warningAuthentication(servletResponse,"用户登录状态已失效,请重新登陆");
            return;
        }catch (MalformedJwtException e){
            log.error("Token校验错误");
            ResponseUtil.warningAuthentication(servletResponse,"token校验错误,用户认证失败");
            return;
        }catch (SignatureException e){
            log.error("签名错误");
            ResponseUtil.warningAuthentication(servletResponse,"Token签名错误,用户认证失败");
            return;
        }


    }

    @Override
    public void destroy() {

    }
}
