package com.beautifulsoup.driving;

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
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DrivingApplicationTests {


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
                .agentIdcardImg("http://39.106.62.161:8888/driving/M00/00/00/111")
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
            String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJhZ2VudE5hbWVcIjpcIkFkbWluXCIsXCJhZ2VudFBhc3N3b3JkXCI6XCJlMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZVwiLFwiYWdlbnRQaG9uZVwiOlwiMTc4NjQxOTUyMDBcIixcImFnZW50RW1haWxcIjpcImJlYXV0aWZ1bHNvdXBAMTYzLmNvbVwiLFwiYWdlbnRJZGNhcmRcIjpcIjM3MjMzMDAwMDAwNzc3NzY2MzIyMFwiLFwiYWdlbnRJZGNhcmRJbWdcIjpcImh0dHA6Ly8zOS4xMDYuNjIuMTYxOjg4ODgvZHJpdmluZy9NMDAvMDAvMDAvMTExXCIsXCJhZ2VudFNjaG9vbFwiOlwi5bGx5Lic5biI6IyD5aSn5a2mXCIsXCJwYXJlbnRJZFwiOi0xLFwic3RhdHVzXCI6MSxcInJvbGVzXCI6W119IiwibmFtZSI6IkJlYXV0aWZ1bFNvdXAiLCJpc3MiOiJCZWF1dGlmdWxTb3VwIiwiYWRtaW4iOnRydWUsImV4cCI6MTU1NDI4MTk2NSwiaWF0IjoxNTU0MjgxOTYwLCJqdGkiOiJqd3QifQ.bkvcadwruzqYC1hq_dV3zzmGfsygBhmp-kwiAgPhToC8PsU7DryaUJrgq9DYAUqknKk3IsCiOc47lriPr2LzPA";
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

}
