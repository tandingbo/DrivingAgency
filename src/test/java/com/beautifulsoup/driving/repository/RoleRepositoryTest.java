package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.dto.RoleDto;
import com.beautifulsoup.driving.pojo.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void saveRoleTest(){
        RoleDto roleDto=RoleDto.builder().roleName("超级管理员")
                .operator("Admin")
                .remark("超级管理员最最高层级,可以添加代理和学员")
                .status(1)
                .type(0).build();
        Role role=new Role();
        BeanUtils.copyProperties(roleDto,role);
        roleRepository.save(role);
    }


}
