package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.dto.RoleDto;
import com.beautifulsoup.driving.pojo.Authority;
import com.beautifulsoup.driving.pojo.Role;
import com.google.common.collect.ImmutableList;
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
        RoleDto roleDto=RoleDto.builder().roleName("二级代理")
                .operator("Admin")
                .remark("二级代理只能招收学员,必须受管理员和相应的一级代理的管理")
                .status(1)
                .type(2).build();
        Role role=new Role();
        BeanUtils.copyProperties(roleDto,role);
        roleRepository.save(role);
    }

    @Test
    public void awardAuthorities(){
        Optional<Role> optionalRole = roleRepository.findById(3);

        if (optionalRole.isPresent()){
            Role role = optionalRole.get();
//            ExampleMatcher matcher=new Ex
//            Example example=Example.of()
            List<Authority> authorityList = authorityRepository.findAllByIdIn(ImmutableList.of(
                    3,8
            ));
            role.setAuthorities(authorityList);
            roleRepository.saveAndFlush(role);
        }


    }

}
