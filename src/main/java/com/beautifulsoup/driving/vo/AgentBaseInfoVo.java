package com.beautifulsoup.driving.vo;

import com.beautifulsoup.driving.pojo.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

//用于首页展示
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentBaseInfoVo implements Serializable {
    private static final long serialVersionUID = -2024608984564029929L;
    private Integer id;
    private String agentName;
    private Integer dailyAchieve;
    private Integer agentAchieve;//总业绩
    private Integer status;//状态。1表示正常，0表示冻结
}
