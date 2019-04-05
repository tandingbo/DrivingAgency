package com.beautifulsoup.driving.controller;

import com.beautifulsoup.driving.common.ResponseResult;
import com.beautifulsoup.driving.dto.StudentDto;
import com.beautifulsoup.driving.service.StudentService;
import com.beautifulsoup.driving.vo.StudentVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping(value = "/listbypage",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<StudentVo>> getAllStudentsByPage(@RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                                         @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){

        List<StudentVo> studentVos=studentService.getAllStudentsByPage(pageNum,pageSize);
        return ResponseResult.createBySuccess("分页获取所有学生信息成功",studentVos);
    }

    @GetMapping(value = "/listall",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<StudentVo>> getAllStudents(){

        List<StudentVo> studentVos=studentService.getAllStudents();
        return ResponseResult.createBySuccess("获取所有学生信息成功",studentVos);
    }

    @GetMapping(value = "/listunexamine",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<StudentVo>> getAllUnExaminedStudents(){
        List<StudentVo> studentVos=studentService.getAllUnExaminedStudents();
        return ResponseResult.createBySuccess("获取未审核的所有学员成功",studentVos);
    }

    @PostMapping(value = "/examine",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<StudentVo> examineExistsAgent(@RequestParam("studentname")String studentname){
        StudentVo studentVo = studentService.examineExistsStudent(studentname);
        if (studentVo != null) {
            return ResponseResult.createBySuccess("学员审核通过",studentVo);
        }
        return ResponseResult.createBySuccess("学员审核不通过");
    }

}
