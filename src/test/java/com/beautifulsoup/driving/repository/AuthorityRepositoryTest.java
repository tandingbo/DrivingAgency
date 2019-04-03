package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.dto.AuthorityDto;
import com.beautifulsoup.driving.pojo.Authority;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void saveAuthorities(){
        AuthorityDto authorityDto=AuthorityDto.builder()
                .aclName("添加学员")
                .aclUrl("/student/add")
                .operator("")
                .remark("所有代理都能添加学员")
                .build();
        Authority authority=new Authority();
        BeanUtils.copyProperties(authorityDto,authority);
        authority.setStatus(1);
        authorityRepository.saveAndFlush(authority);
    }

}
