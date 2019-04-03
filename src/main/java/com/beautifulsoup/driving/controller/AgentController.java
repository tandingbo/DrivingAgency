package com.beautifulsoup.driving.controller;

import com.beautifulsoup.driving.common.ResponseResult;
import com.beautifulsoup.driving.vo.AgentBaseInfoVo;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "/agent",description = "代理操作",protocols = "http")
@Controller
@RequestMapping(value = "/agent")
public class AgentController {


    @PostMapping(value = "/admin/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseResult<AgentBaseInfoVo> adminLogin(@RequestParam("username")String username,
                                                      @RequestParam("password")String password){


        return null;
    }

}
