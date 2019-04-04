package com.beautifulsoup.driving.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleCode {
    ROLE_ADMIN(0,"超级管理员"),
    ROLE_FIRST_TIER_AGENT(1,"一级代理"),
    ROLE_SECOND_TIER_AGENT(2,"二级代理")
    ;
    private final Integer type;
    private final String roleName;
}
