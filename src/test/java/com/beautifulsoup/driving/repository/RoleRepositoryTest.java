package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.dto.RoleDto;
import com.beautifulsoup.driving.pojo.Authority;
import com.beautifulsoup.driving.pojo.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void saveRoleTest(){
        RoleDto roleDto=RoleDto.builder().roleName("超级管理员")
                .operator("AgentFirst")
                .remark("")
                .status(1)
                .type(0).build();
        Role role=new Role();
        BeanUtils.copyProperties(roleDto,role);
        roleRepository.save(role);
    }

    @Test
    public void awardAuthorities(){
        Optional<Role> optionalRole = roleRepository.findById(1);

        if (optionalRole.isPresent()){
            Role role = optionalRole.get();
            List<Authority> authorityList = authorityRepository.findAll();
            role.setAuthorities(authorityList);
            roleRepository.saveAndFlush(role);
        }


    }

}
