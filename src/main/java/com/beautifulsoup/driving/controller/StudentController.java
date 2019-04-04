package com.beautifulsoup.driving.controller;

import com.beautifulsoup.driving.common.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "/student",description = "学员操作",protocols = "http")
@Controller
@RequestMapping(value = "/student")
public class StudentController {

    @PostMapping(value = "/add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult addStudent(){
        return null;
    }


}
