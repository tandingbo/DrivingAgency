package com.beautifulsoup.driving.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

//用于排行榜展示
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentRankingVo implements Serializable {
    private static final long serialVersionUID = -809744186738697050L;
    private Integer id;
    private String agentName;
    private Integer dailyAchieve;
    private Integer agentAchieve;//总业绩
    private Integer starNums;
    @JsonProperty(value = "comments")
    private List<CommentVo> commentVos;
}
