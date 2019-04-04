package com.beautifulsoup.driving.vo;

import com.beautifulsoup.driving.pojo.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentBaseInfoVo {
    private Integer id;
    private String agentName;
    private String agentPassword;
    private String agentPhone;
    private String agentEmail;
    private String agentIdcard;
    private String agentIdcardImg;
    private String agentSchool;
    private Integer agentAchieve;//总业绩
    private Integer parentId;//超级管理员父节点不存在,为-1.
    private Integer status;//状态。1表示正常，0表示冻结
    private Role role;
}
