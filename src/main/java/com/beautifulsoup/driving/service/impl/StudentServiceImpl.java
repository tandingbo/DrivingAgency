package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.StudentDto;
import com.beautifulsoup.driving.enums.AgentStatus;
import com.beautifulsoup.driving.enums.RoleCode;
import com.beautifulsoup.driving.enums.StudentStatus;
import com.beautifulsoup.driving.exception.AuthenticationException;
import com.beautifulsoup.driving.exception.ParamException;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.pojo.Student;
import com.beautifulsoup.driving.repository.AgentRepository;
import com.beautifulsoup.driving.repository.StudentRepository;
import com.beautifulsoup.driving.service.AgentManageService;
import com.beautifulsoup.driving.service.StudentService;
import com.beautifulsoup.driving.utils.ParamValidatorUtil;
import com.beautifulsoup.driving.vo.AgentVo;
import com.beautifulsoup.driving.vo.StudentVo;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.management.relation.RoleStatus;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentManageService agentManageService;

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

        if (authentication.getStatus().equals(AgentStatus.UNEXAMINED.getCode())){
            throw new AuthenticationException("当前账户审核未通过,不能添加学员");
        }

        student.setOperator(authentication.getAgentName());
        if (!authentication.getRole().getType().equals(RoleCode.ROLE_ADMIN.getType())){
            student.setStatus(StudentStatus.UNAVAILABLE.getStatus());
        }else{
            student.setStatus(StudentStatus.AVAILABLE.getStatus());
        }

        studentRepository.save(student);
        stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_DAILY,
                DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),1);
        stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,
                DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),1);

        stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_TOTAL_ORDER,
                DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),
                Double.parseDouble(Strings.nullToEmpty((String) stringRedisTemplate.opsForHash()
                        .get(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()))));
        stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_DAILY_ORDER,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()
                ,Double.parseDouble(Strings.nullToEmpty((String) stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_DAILY,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()))));


        if (authentication.getRole().getType().equals(RoleCode.ROLE_SECOND_TIER_AGENT.getType())){
            Agent parent = agentRepository.findById(authentication.getParentId()).get();
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

    @Override
    public List<StudentVo> getAllStudentsByPage(Integer pageNum, Integer pageSize) {
        List<String> collect = agentManageService.listAllAgents().stream().map(AgentVo::getAgentName).collect(Collectors.toList());
        Pageable pageable= PageRequest.of(pageNum-1,pageSize, Sort.by(Sort.Order.desc("studentPrice")));
        Page<Student> all = studentRepository.findAllByOperatorIn(collect,pageable);
        List<StudentVo> studentVos=Lists.newArrayList();
        all.get().forEach(student -> {
            StudentVo studentVo=new StudentVo();
            BeanUtils.copyProperties(student,studentVo);
            studentVos.add(studentVo);
        });
        return studentVos;
    }

    @Override
    public List<StudentVo> getAllStudents() {
        List<String> collect = agentManageService.listAllAgents().stream().map(AgentVo::getAgentName).collect(Collectors.toList());
        List<Student> all = studentRepository.findAllByOperatorIn(collect,Sort.by(Sort.Order.desc("studentPrice")));
        List<StudentVo> studentVos= Lists.newArrayList();


        all.forEach(student -> {
            StudentVo studentVo=new StudentVo();
            BeanUtils.copyProperties(student,studentVo);
            studentVos.add(studentVo);
        });
        return studentVos;
    }

    @Override
    public StudentVo examineExistsStudent(String studentname) {
        Student byStudentName = studentRepository.findByStudentName(studentname);
        if (byStudentName != null) {
            if (byStudentName.getStatus().equals(StudentStatus.UNAVAILABLE.getStatus())){
                byStudentName.setStatus(StudentStatus.AVAILABLE.getStatus());
                studentRepository.save(byStudentName);
            }
            StudentVo studentVo=new StudentVo();
            BeanUtils.copyProperties(byStudentName,studentVo);
            return studentVo;
        }
        return null;
    }

    @Override
    public List<StudentVo> getAllUnExaminedStudents() {
        List<StudentVo> studentVos=Lists.newArrayList();
        List<Student> list = studentRepository.findAllByStatus(StudentStatus.UNAVAILABLE.getStatus());
        for (Student student : list) {
            StudentVo studentVo=new StudentVo();
            BeanUtils.copyProperties(student,studentVo);
            studentVos.add(studentVo);
        }
        return studentVos;
    }
}
