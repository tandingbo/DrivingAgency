package com.beautifulsoup.driving.controller;

import com.beautifulsoup.driving.common.ResponseResult;
import com.beautifulsoup.driving.dto.StudentDto;
import com.beautifulsoup.driving.pojo.Student;
import com.beautifulsoup.driving.service.StudentService;
import com.beautifulsoup.driving.vo.StudentVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Api(tags = "/student",description = "学员操作",protocols = "http")
@Controller
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @PostMapping(value = "/add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<StudentVo> addStudent(@Valid @RequestBody StudentDto studentDto, BindingResult result){
        StudentVo studentVo=studentService.addNewStudent(studentDto,result);
        if (studentVo != null) {
            return ResponseResult.createBySuccess("学员添加成功",studentVo);
        }
        return ResponseResult.createByError("学员添加失败");
    }


}
