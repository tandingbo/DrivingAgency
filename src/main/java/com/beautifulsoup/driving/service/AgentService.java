package com.beautifulsoup.driving.service;

import com.beautifulsoup.driving.vo.AgentBaseInfoVo;

public interface AgentService {
    AgentBaseInfoVo adminLogin(String username,String password);
}
