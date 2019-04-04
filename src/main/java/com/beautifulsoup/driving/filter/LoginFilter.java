package com.beautifulsoup.driving.filter;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.UserTokenDto;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.utils.JsonSerializerUtil;
import com.beautifulsoup.driving.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.security.sasl.AuthenticationException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@WebFilter(value = "/**")
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
            throw new AuthenticationException("用户未登录,请登录");
        }
        try {
            Claims claims = TokenUtil.parseJWT(token);
            String subject = claims.getSubject();
            log.info(subject);
            UserTokenDto userTokenDto= JsonSerializerUtil.string2Obj(subject,UserTokenDto.class);

            if (userTokenDto.getParentId()==-1){
                //管理员账户
                Boolean hasAdmin = redisTemplate.opsForHash().hasKey(DrivingConstant.Redis.LOGIN_AGENTS,
                        DrivingConstant.Redis.ADMIN_TOKEN + token);
                if (!hasAdmin.booleanValue()) {
                    Agent agent1 = agentRepository.findAgentByAgentNameAndParentId(userTokenDto.getAgentName(),-1);
                    if (agent1==null){
                        throw new AuthenticationException("服务器错误,您的账号已被盗?");
                    }
                    agent1.setAgentPassword(null);
                    redisTemplate.opsForHash().put(DrivingConstant.Redis.LOGIN_AGENTS,
                            DrivingConstant.Redis.ADMIN_TOKEN+token,agent1);
                }
               Agent agent=(Agent) redisTemplate.opsForHash().get(DrivingConstant.Redis.LOGIN_AGENTS,
                        DrivingConstant.Redis.ADMIN_TOKEN+token);
               SecurityContextHolder.addAgent(agent);

            }else{

            }

            chain.doFilter(request,response);
            return;
        }catch (ExpiredJwtException e){
            log.error("Token已失效");
            Agent agent=(Agent) redisTemplate.opsForHash().get(DrivingConstant.Redis.LOGIN_AGENTS,
                    DrivingConstant.Redis.ADMIN_TOKEN+token);
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
                ((HttpServletResponse) response).setHeader("Cache-Control","no-store");
                ((HttpServletResponse) response).setHeader("token",newToken);
                Long oldExpire = stringRedisTemplate.getExpire(DrivingConstant.Redis.TOKEN_REFRESH + token, TimeUnit.SECONDS);
                stringRedisTemplate.opsForValue().set(DrivingConstant.Redis.TOKEN_REFRESH+newToken,UUID.randomUUID().toString());
                stringRedisTemplate.expire(DrivingConstant.Redis.TOKEN_REFRESH+newToken,oldExpire,TimeUnit.SECONDS);
                stringRedisTemplate.delete(DrivingConstant.Redis.TOKEN_REFRESH + token);
                //旧Token加入黑名单
                stringRedisTemplate.opsForValue().set(DrivingConstant.Redis.TOKEN_REFRESH + token,newToken);
                stringRedisTemplate.expire(DrivingConstant.Redis.TOKEN_REFRESH + token,30,TimeUnit.SECONDS);
                chain.doFilter(request,response);
                return;
            }
            throw new AuthenticationException("用户登录状态已失效,请重新登陆");
        }catch (MalformedJwtException e){
            log.error("Token校验错误");
            throw new AuthenticationException("token校验错误,用户认证失败");
        }catch (SignatureException e){
            log.error("签名错误");
            throw new AuthenticationException("Token签名错误,用户认证失败");
        }


    }

    @Override
    public void destroy() {

    }
}
