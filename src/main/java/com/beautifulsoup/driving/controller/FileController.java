package com.beautifulsoup.driving.controller;

import com.beautifulsoup.driving.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(tags = "/file",description = "文件操作",protocols = "http")
@Controller
@RequestMapping(value = "/file")
public class FileController {

    @ApiOperation(value = "上传多个文件",notes = "上传多个文件",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = List.class,httpMethod = "Post")
    @ApiResponses({
            @ApiResponse(code = 100, message = "请求参数有误"),
            @ApiResponse(code = 101, message = "未授权"),
            @ApiResponse(code = 103, message = "禁⽌访问"),
            @ApiResponse(code = 104, message = "请求路径不存在"),
            @ApiResponse(code = 200, message = "服务器内部错误"),
            @ApiResponse(code = 400,message = "请求参数不正确")
    })
    @PostMapping(value = "/uploads",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult uploadFiles(){

        return null;
    }

}
