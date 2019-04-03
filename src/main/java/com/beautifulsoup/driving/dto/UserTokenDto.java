package com.beautifulsoup.driving.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenDto {
    private String agentName;
    private Integer parentId;//超级管理员父节点不存在,为-1.
    private Integer status;//状态。1表示正常，0表示冻结
    private Integer agentAchieve;//总业绩
    private String agentSchool;
}
