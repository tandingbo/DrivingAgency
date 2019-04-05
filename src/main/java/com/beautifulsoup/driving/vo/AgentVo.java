package com.beautifulsoup.driving.vo;

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
public class AgentVo {
    private Integer id;
    private String agentName;
    private String agentPhone;
    private String agentEmail;
    private String agentIdcard;
    private String agentIdcardImg;
    private String agentSchool;
    private Integer dailyAchieve;
    private Integer agentAchieve;//总业绩
    private Integer status;//状态。1表示正常，0表示冻结
    private RoleVo roleVo;
}
