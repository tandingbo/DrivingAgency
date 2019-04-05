package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.dto.StudentDto;
import com.beautifulsoup.driving.vo.StudentVo;
import org.springframework.validation.BindingResult;

public interface StudentService {
    StudentVo addNewStudent(StudentDto studentDto, BindingResult result);
}
