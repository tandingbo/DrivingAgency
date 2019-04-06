package com.beautifulsoup.driving.controller;

import com.beautifulsoup.driving.common.ResponseResult;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.AnnouncementDto;
import com.beautifulsoup.driving.dto.CommentDto;
import com.beautifulsoup.driving.pojo.Comment;
import com.beautifulsoup.driving.service.AgentManageService;
import com.beautifulsoup.driving.vo.*;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @GetMapping(value = "/agent/listall",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<AgentVo>> listAllAgents(){
        List<AgentVo> agents=agentManageService.listAllAgents();
        return ResponseResult.createBySuccess("查看代理成功",agents);
    }

    @GetMapping(value = "/agent/listbydaily",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<AgentBaseInfoVo>> listAllAgentsByDailyAchievements(){
        List<AgentBaseInfoVo> agents=agentManageService.listAllAgentsByDailyAchievements();
        return ResponseResult.createBySuccess("代理日业绩获取成功",agents);
    }

    @GetMapping(value = "/agent/listbytotal",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<AgentBaseInfoVo>> listAllAgentsByTotalAchievements(){
        List<AgentBaseInfoVo> agents=agentManageService.listAllAgentsByTotalAchievements();
        return ResponseResult.createBySuccess("代理总业绩获取成功",agents);
    }

    //    agent/listbyname
    @GetMapping(value = "/agent/listbyname",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<AgentBaseInfoVo>> listChildrenAgentsByName(@RequestParam("username")String username){
        List<AgentBaseInfoVo> agents=agentManageService.listChildrenAgentsByName(username);
        return ResponseResult.createBySuccess("子代理总业绩获取成功",agents);
    }

    @GetMapping(value = "/agent/unexamine/listall",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
            return ResponseResult.createBySuccess("发布公告成功",announcementVo);
        }

        return ResponseResult.createByError("发布公告失败");
    }

    @GetMapping(value = "/announcement/getlatest",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AnnouncementVo> getLatestAnnouncement(){
        AnnouncementVo announcementVo=agentManageService.getLatestAnnouncement();
        return ResponseResult.createBySuccess("最新公告获取成功",announcementVo);
    }

    @GetMapping(value = "/ranking/listbydaily",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<AgentRankingVo>> rankingListbyDailyAchievements(){
        List<AgentRankingVo> agents=agentManageService.rankingListbyDailyAchievements();
        return ResponseResult.createBySuccess("排行榜日业绩获取成功",agents);
    }

    @GetMapping(value = "/ranking/listbytotal",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<AgentRankingVo>> rankingListbyTotalAchievements(){
        List<AgentRankingVo> agents=agentManageService.rankingListbyTotalAchievements();
        return ResponseResult.createBySuccess("排行榜总业绩获取成功",agents);
    }

    @GetMapping(value = "/ranking/comments/list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<List<CommentVo>> rankingCommentsListByName(@RequestParam("username")String username){
        List<CommentVo> commentVos=agentManageService.rankingCommentsListByName(username);
        return ResponseResult.createBySuccess("评论获取成功",commentVos);
    }


    @PostMapping(value = "/agent/star",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentRankingVo> starAgentByName(@RequestParam(value = "username")String username){
        AgentRankingVo agentRankingVo=agentManageService.starAgent(username);
        return ResponseResult.createBySuccess(String.join(" ",username,"点赞成功"),agentRankingVo);
    }
    @PostMapping(value = "/agent/comment",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentRankingVo> publishCommentByAgentName(@Valid @RequestBody CommentDto commentDto,BindingResult result){
        AgentRankingVo agentRankingVo=agentManageService.publishCommentByAgentName(commentDto,result);
        return ResponseResult.createBySuccess("评论发表成功",agentRankingVo);
    }

    @GetMapping(value = "/derived/excel",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<String> derivedExcel(){
        String path=agentManageService.derivedExcel();
        if (StringUtils.isNotBlank(path)){
            return ResponseResult.createBySuccess("学员信息导出Excel成功",path);
        }
        return ResponseResult.createByError("学员信息导出Excel失败");
    }


}
