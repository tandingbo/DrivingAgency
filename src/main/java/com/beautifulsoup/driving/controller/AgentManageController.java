package com.beautifulsoup.driving.controller;

import com.beautifulsoup.driving.common.ResponseResult;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.service.AgentManageService;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "/manage",description = "代理操作",protocols = "http")
@Controller
@RequestMapping(value = "/manage")
public class AgentManageController {

    @Autowired
    private AgentManageService agentManageService;

    @PostMapping(value = "/agent/add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentBaseInfoVo> addNewAgent(@Valid @RequestBody AgentDto agentDto,
                                                       BindingResult result){
        AgentBaseInfoVo agentBaseInfoVo = agentManageService.addNewAgent(agentDto, result);
        if (agentBaseInfoVo != null) {
            return ResponseResult.createBySuccess("代理添加成功",agentBaseInfoVo);
        }
        return ResponseResult.createByError("代理添加失败");
    }

    @PostMapping(value = "/agent/check",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentBaseInfoVo> checkExistsAgent(@Valid @RequestBody AgentDto agentDto,
                                                       BindingResult result){
        AgentBaseInfoVo agentBaseInfoVo = agentManageService.addNewAgent(agentDto, result);
        if (agentBaseInfoVo != null) {
            return ResponseResult.createBySuccess("代理添加成功",agentBaseInfoVo);
        }
        return ResponseResult.createByError("代理添加失败");
    }


    @PostMapping(value = "/publish/announcement",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult publishAnnouncement(){
        return null;
    }

    @GetMapping(value = "/derived/excel")
    @ResponseBody
    public ResponseResult derivedExcel(){
        return null;
    }


}
