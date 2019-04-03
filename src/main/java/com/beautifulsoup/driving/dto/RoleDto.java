package com.beautifulsoup.driving.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    @Length(max=100,message = "角色名称长度不能超过100字符")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    private Integer type;

    private Integer status;

    @Length(max=500,message = "备注信息必须在500字符之内")
    private String remark;

    @Length(max=100,message = "操作者名长度必须在100字符之内")
    @NotBlank(message = "操作者名不能为空")
    private String operator;
}
