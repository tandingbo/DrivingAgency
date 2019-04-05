package com.beautifulsoup.driving;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.UserTokenDto;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.repository.AgentRepositoryTest;
import com.beautifulsoup.driving.utils.MD5Util;
import com.beautifulsoup.driving.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@WebAppConfiguration
public class DrivingApplicationTests {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void contextLoads() {
        AgentDto agentDto=AgentDto.builder()
                .agentName("Admin")
//                .agentPassword(MD5Util.MD5Encode("123456"))
                .agentEmail("beautifulsoup@163.com")
                .agentPhone("17864195200")
//                .status(1)//状态正常,可用
//                .parentId(-1)//1级代理
                .agentIdcard("372330000007777663220")
                .agentSchool("山东师范大学")
                .agentIdcardImg("http://127.0.0.1:8888/driving/M00/00/00/111")
                .build();
        Agent agent=new Agent();
        BeanUtils.copyProperties(agentDto,agent);
        UserTokenDto userTokenDto=new UserTokenDto();
        BeanUtils.copyProperties(agentDto,userTokenDto);
        String token = TokenUtil.conferToken(userTokenDto, 5000L);
        log.error(token);
        try {
            Claims claims = TokenUtil.parseJWT(token);

            log.info(claims.getId());
            log.info(claims.getIssuer());
            log.info(claims.getSubject());
            log.info(claims.getIssuedAt().toString());
            log.info(claims.getExpiration().toString());
            log.info(claims.getExpiration().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseJWT(){
        try{
            String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJhZ2VudE5hbWVcIjpcIkFkbWluXCIsXCJwYXJlbnRJZFwiOi0xLFwic3RhdHVzXCI6MSxcImFnZW50U2Nob29sXCI6XCLlsbHkuJzluIjojIPlpKflraZcIn0iLCJuYW1lIjoiQmVhdXRpZnVsU291cCIsImlzcyI6IkJlYXV0aWZ1bFNvdXAiLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTU0MzU4MDA4LCJpYXQiOjE1NTQzNTA4MDgsImp0aSI6Imp3dCJ9.n6036PDZ2FijD9KkeX1tRHXhjTHcqqDl5ynviDfqy9FpkpYTEEJZkPPA4OUqEIPjulmSY0RGJlrfXXAR9nbY7g";
            String token2="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJhZ2VudE5hbWVcIjpcIkFkbWluXCIsXCJwYXJlbnRJZFwiOi0xLFwic3RhdHVzXCI6MSxcImFnZW50U2Nob29sXCI6XCLlsbHkuJzluIjojIPlpKflraZcIn0iLCJuYW1lIjoiQmVhdXRpZnVsU291cCIsImlzcyI6IkJlYXV0aWZ1bFNvdXAiLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTU0MzYwNDEzLCJpYXQiOjE1NTQzNTMyMTMsImp0aSI6Imp3dCJ9.4Nae2EthLjuThyn48jRmwcYjSBwR3E2grBJZoisrrWw0gfYF_0Yq4JxtM2SHpX15G3V_eZDLwRHuFUnvkCT8xQ";
            Claims claims = TokenUtil.parseJWT(token);
            log.info(claims.getId());
            log.info(claims.getIssuer());
            log.info(claims.getSubject());
            log.info(claims.getIssuedAt().toString());
            log.info(claims.getExpiration().toString());
            log.info(claims.getExpiration().toString());
        }catch (SignatureException e){
            log.error("签名错误");
        }catch (ExpiredJwtException e){
            log.error("token 已失效");
        }catch (MalformedJwtException e){
            log.error("token 校验错误");
        }
    }

    @Test
    public void expireKey(){
        String uuid=UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(uuid,uuid);
        stringRedisTemplate.expire(uuid,3600, TimeUnit.SECONDS);
    }

    @Test
    public void md5Password(){
//        String encode = MD5Util.MD5Encode("123456");
//        log.info(encode);
//        String token1="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJhZ2VudE5hbWVcIjpcIkFkbWluXCIsXCJwYXJlbnRJZFwiOi0xLFwic3RhdHVzXCI6MSxcImFnZW50U2Nob29sXCI6XCLlsbHkuJzluIjojIPlpKflraZcIn0iLCJuYW1lIjoiQmVhdXRpZnVsU291cCIsImlzcyI6IkJlYXV0aWZ1bFNvdXAiLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTU0MzU1OTcxLCJpYXQiOjE1NTQzNDg3NzEsImp0aSI6Imp3dCJ9.piIeUfJkyy0sq_iFe6qlTn5OU3Kj7S55imyyj4t1El2oNvHE2j0j9EbF2sf9iU24JtFYpmF0Klf0MWTgt8QV7Q";
//        String token2="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJhZ2VudE5hbWVcIjpcIkFkbWluXCIsXCJwYXJlbnRJZFwiOi0xLFwic3RhdHVzXCI6MSxcImFnZW50U2Nob29sXCI6XCLlsbHkuJzluIjojIPlpKflraZcIn0iLCJuYW1lIjoiQmVhdXRpZnVsU291cCIsImlzcyI6IkJlYXV0aWZ1bFNvdXAiLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTU0MzU2MDY3LCJpYXQiOjE1NTQzNDg4NjcsImp0aSI6Imp3dCJ9.XLM71GlcPltBjBiBgYQixL95Bp-q7Q-l-60-y6i2zQFVABIjfZU9ZGRMQh3Gao262D_KPGFyKMnHQBALumD1WQ";
        stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_DAILY,
                DrivingConstant.Redis.ACHIEVEMENT_AGENT+"aaa",1);
        log.info("aaa:"+stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_DAILY,
                DrivingConstant.Redis.ACHIEVEMENT_AGENT+"aaa"));
        stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_DAILY_ORDER,DrivingConstant.Redis.ACHIEVEMENT_AGENT
        +"aaa", Double.parseDouble((String) stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_DAILY,
                DrivingConstant.Redis.ACHIEVEMENT_AGENT+"aaa")) );
    }

}
