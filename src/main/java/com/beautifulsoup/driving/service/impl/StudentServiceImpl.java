package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.StudentDto;
import com.beautifulsoup.driving.enums.StudentStatus;
import com.beautifulsoup.driving.exception.ParamException;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.pojo.Student;
import com.beautifulsoup.driving.repository.StudentRepository;
import com.beautifulsoup.driving.service.StudentService;
import com.beautifulsoup.driving.utils.ParamValidatorUtil;
import com.beautifulsoup.driving.vo.StudentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentVo addNewStudent(StudentDto studentDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);

        Student byStudentName = studentRepository.findByStudentName(studentDto.getStudentName());

        if (byStudentName != null) {
            throw new ParamException("学员信息已经注册过,请勿重复注册");
        }

        Student student=new Student();
        BeanUtils.copyProperties(studentDto,student);
        Agent agent = SecurityContextHolder.getAgent();
        student.setOperator(agent.getAgentName());
        student.setStatus(StudentStatus.AVAILABLE.getStatus());
        studentRepository.save(student);
        StudentVo studentVo=new StudentVo();
        BeanUtils.copyProperties(student,studentVo);
        return studentVo;
    }
}
