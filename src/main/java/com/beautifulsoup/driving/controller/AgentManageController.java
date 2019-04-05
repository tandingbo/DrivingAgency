package com.beautifulsoup.driving.controller;

import com.beautifulsoup.driving.common.ResponseResult;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.AnnouncementDto;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.pojo.Announcement;
import com.beautifulsoup.driving.service.AgentManageService;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.beautifulsoup.driving.vo.AgentVo;
import com.beautifulsoup.driving.vo.AnnouncementVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping(value = "/agent/listall")
    @ResponseBody
    public ResponseResult<List<AgentVo>> listAllAgents(){
        List<AgentVo> agents=agentManageService.listAllAgents();
        return ResponseResult.createBySuccess("查看代理成功",agents);
    }

    @GetMapping(value = "/agent/unexamine/listall")
    @ResponseBody
    public ResponseResult<List<AgentVo>> listAllUnExamineAgents(){
        List<AgentVo> agents=agentManageService.listAllUnExamineAgents();
        return ResponseResult.createBySuccess("查看未审核的代理成功",agents);
    }

    @PostMapping(value = "/agent/examine",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentBaseInfoVo> examineExistsAgent(@RequestParam("username")String username){
        AgentBaseInfoVo agentBaseInfoVo = agentManageService.examineExistsAgent(username);
        if (agentBaseInfoVo != null) {
            return ResponseResult.createBySuccess("代理审核通过",agentBaseInfoVo);
        }
        return ResponseResult.createBySuccess("代理审核不通过");
    }


    @PostMapping(value = "/announcement/publish",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AnnouncementVo> publishAnnouncement(@Valid @RequestBody AnnouncementDto announcementDto,
                                                              BindingResult result){
        AnnouncementVo announcementVo = agentManageService.publishAnnouncement(announcementDto, result);
        if (announcementVo != null) {
            return ResponseResult.createBySuccess("获取最新公告成功",announcementVo);
        }

        return ResponseResult.createByError("获取最新公告失败");
    }

    @GetMapping(value = "/announcement/getlatest")
    @ResponseBody
    public ResponseResult<AnnouncementVo> getLatestAnnouncement(){
        AnnouncementVo announcementVo=agentManageService.getLatestAnnouncement();
        return ResponseResult.createBySuccess("最新公告获取成功",announcementVo);
    }

    @GetMapping(value = "/derived/excel")
    @ResponseBody
    public ResponseResult derivedExcel(){
        return null;
    }


}
