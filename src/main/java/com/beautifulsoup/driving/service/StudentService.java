package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.StudentDto;
import com.beautifulsoup.driving.vo.StudentVo;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface StudentService {
    StudentVo addNewStudent(StudentDto studentDto, BindingResult result);
    List<StudentVo> getAllStudentsByPage(Integer pageNum, Integer pageSize);
    List<StudentVo> getAllStudents();
    StudentVo examineExistsStudent(String studentname);
    List<StudentVo> getAllUnExaminedStudents();
}
