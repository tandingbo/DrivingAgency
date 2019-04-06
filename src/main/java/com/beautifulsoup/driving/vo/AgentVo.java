package com.beautifulsoup.driving.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

//用于管理信息展示
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentVo implements Serializable {
    private static final long serialVersionUID = -2236167266141228595L;
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
