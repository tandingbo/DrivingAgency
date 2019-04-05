package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.StudentDto;
import com.beautifulsoup.driving.enums.RoleCode;
import com.beautifulsoup.driving.enums.StudentStatus;
import com.beautifulsoup.driving.exception.ParamException;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.pojo.Student;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.repository.StudentRepository;
import com.beautifulsoup.driving.service.StudentService;
import com.beautifulsoup.driving.utils.ParamValidatorUtil;
import com.beautifulsoup.driving.vo.StudentVo;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public StudentVo addNewStudent(StudentDto studentDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);

        Student byStudentName = studentRepository.findByStudentName(studentDto.getStudentName());

        if (byStudentName != null) {
            throw new ParamException("学员信息已经注册过,请勿重复注册");
        }

        Student student=new Student();
        BeanUtils.copyProperties(studentDto,student);
        Agent authentication = SecurityContextHolder.getAgent();
        student.setOperator(authentication.getAgentName());
        student.setStatus(StudentStatus.AVAILABLE.getStatus());
        studentRepository.save(student);
        stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_DAILY,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),1);
        stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),1);

        stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_TOTAL_ORDER,
                DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),Double.parseDouble(Strings.nullToEmpty((String) stringRedisTemplate.opsForHash()
                        .get(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()))));
        stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_DAILY_ORDER,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()
                ,Double.parseDouble(Strings.nullToEmpty((String) stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_DAILY,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()))));


        if (authentication.getRole().getType().equals(RoleCode.ROLE_SECOND_TIER_AGENT.getType())){
            Agent parent = agentRepository.findByParentId(authentication.getParentId());
            stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_DAILY,DrivingConstant.Redis.ACHIEVEMENT_AGENT+parent.getAgentName(),1);
            stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,DrivingConstant.Redis.ACHIEVEMENT_AGENT+parent.getAgentName(),1);

            stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_TOTAL_ORDER,
                    DrivingConstant.Redis.ACHIEVEMENT_AGENT+parent.getAgentName(),
                    Double.parseDouble(MoreObjects.firstNonNull(Strings.emptyToNull((String) stringRedisTemplate.opsForHash()
                            .get(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,DrivingConstant.Redis.ACHIEVEMENT_AGENT+parent.getAgentName())),"0")));
            stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_DAILY_ORDER,DrivingConstant.Redis.ACHIEVEMENT_AGENT+parent.getAgentName()
                    ,Double.parseDouble(MoreObjects.firstNonNull(Strings.emptyToNull((String) stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_DAILY,
                            DrivingConstant.Redis.ACHIEVEMENT_AGENT+parent.getAgentName())),"0")));

        }


        StudentVo studentVo=new StudentVo();
        BeanUtils.copyProperties(student,studentVo);
        return studentVo;
    }
}
