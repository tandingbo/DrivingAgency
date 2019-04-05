package com.beautifulsoup.driving.controller;

import com.beautifulsoup.driving.common.ResponseResult;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.AgentNewDto;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.service.AgentService;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import com.beautifulsoup.driving.vo.AgentVo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Api(tags = "/agent",description = "账户信息",protocols = "http")
@Controller
@RequestMapping(value = "/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentBaseInfoVo> adminLogin(@RequestParam("username")String username,
                                                      @RequestParam("password")String password, HttpServletResponse response){

        AgentBaseInfoVo baseInfoVo = agentService.login(username, password,response);

        if (baseInfoVo != null) {
            return ResponseResult.createBySuccess("用户登录成功",baseInfoVo);
        }

        return ResponseResult.createByError("用户登录失败");
    }

    @PostMapping(value = "/logout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentBaseInfoVo> logout(@RequestHeader(value = "token",required = true)String token){
        AgentBaseInfoVo baseInfoVo = agentService.logout(token);
        if (baseInfoVo != null) {
            return ResponseResult.createBySuccess("登出成功",baseInfoVo);
        }
        return ResponseResult.createByError("登出失败");
    }

    @PostMapping(value = "/password/reset",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentBaseInfoVo> passwordReset(@RequestHeader(value = "token",required = true)String token
    ,@RequestParam("username")String username,@RequestParam("newPassword")String newPassword,
                                                         @RequestParam("password")String password
    ,@RequestParam("code")String validateCode){
        AgentBaseInfoVo baseInfoVo = agentService.resetPassword(token,username,newPassword,password,validateCode);
        if (baseInfoVo != null) {
            return ResponseResult.createBySuccess("密码重置成功",baseInfoVo);
        }
        return ResponseResult.createByError("密码重置失败");
    }

    @PostMapping(value = "/sendmail",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<Map<String,String>> sendEmail(@RequestParam("username")String username,@RequestParam("email") String email){
        String result = agentService.sendEmail(username,email);
        if (StringUtils.isNotBlank(result)){
            Map<String,String> res= Maps.newHashMap();
            res.put("data",result);
            return ResponseResult.createBySuccess("邮件发送成功",res);
        }
        return ResponseResult.createByError("邮件发送失败");
    }

    @PutMapping(value = "/update",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentBaseInfoVo> updateAgentInfo(@Valid @RequestBody AgentNewDto agentNewDto,
                                                           BindingResult result,
                                                           @RequestHeader("token")String token){

        AgentBaseInfoVo agentBaseInfoVo = agentService.updateAgentInfo(agentNewDto, result,token);

        if (agentBaseInfoVo != null) {
            return ResponseResult.createBySuccess("信息修改成功",agentBaseInfoVo);
        }

        return ResponseResult.createByError("信息修改失败");
    }

    @GetMapping(value = "/get",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<Agent> getAgentInfo(){
        Agent agent=agentService.getAgentInfo();
        if (agent != null) {
            return ResponseResult.createBySuccess("账户信息获取成功",agent);
        }
        return ResponseResult.createByError("账户信息获取失败");
    }

    @PostMapping(value = "/password/retrieve",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentVo> retrievePassword(@RequestParam("username")String username,
                                                    @RequestParam("password")String password,
                                                    @RequestParam("code")String validateCode){
        AgentVo agent=agentService.retrievePassword(username,password,validateCode);
        if (agent != null) {
            return ResponseResult.createBySuccess("密码重置成功",agent);
        }
        return ResponseResult.createByError("密码重置失败");
    }
}
