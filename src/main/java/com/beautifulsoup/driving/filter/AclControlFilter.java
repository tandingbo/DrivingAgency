package com.beautifulsoup.driving.filter;


import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.enums.RoleCode;
import com.beautifulsoup.driving.pojo.Agent;
import com.beautifulsoup.driving.pojo.Authority;
import com.beautifulsoup.driving.utils.ResponseUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Order(value = 2)
@WebFilter(value = {"/manage/*","/student/*"},
        filterName = "aclControlFilter")
public class AclControlFilter implements Filter {

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        exclusionUrlSet=Sets.newConcurrentHashSet();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest= (HttpServletRequest) request;
        HttpServletResponse servletResponse= (HttpServletResponse) response;
        Agent agent=SecurityContextHolder.getAgent();
        if (agent == null) {
            ResponseUtil.warningAccessDenied(servletResponse,"用户信息获取失败");
            return;
        }
        if (!hasUrlAcl(servletRequest.getServletPath())){
            ResponseUtil.warningAccessDenied(servletResponse,"对不起,您没有权限访问");
            return;
        }
        chain.doFilter(servletRequest,servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }

    private boolean isSuperAdmin(){
        Agent agent=SecurityContextHolder.getAgent();
        if (agent.getRole().getType()== RoleCode.ROLE_ADMIN.getType()){
            return true;
        }
        return false;
    }

    private boolean hasUrlAcl(String url){
        if (isSuperAdmin()){
            return true;
        }
        Agent agent=SecurityContextHolder.getAgent();
        if (agent != null) {
            List<Authority> authorities = agent.getRole().getAuthorities();
            for (Authority authority:authorities){
                if (authority.getAclUrl().contains(url)){
                    return true;
                }
            }
        }
        return false;
    }
}
