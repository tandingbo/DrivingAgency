package com.beautifulsoup.driving.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto {

    @Length(max=100,message = "权限名称长度不能超过100字符")
    @NotBlank(message = "权限名称不能为空")
    private String aclName;//权限名称
    @Length(max=500,message = "权限url长度不能超过500字符")
    @NotBlank(message = "权限url不能为空")
    private String aclUrl;//权限对应url

//    private Integer type;//权限类型,1表示对一级代理的操作,2表示对2级代理的操作,3表示对学员的操作
//
//    private Integer status;//状态,1表示正常0表示冻结

    @Length(max=500,message = "备注长度不能超过500字符")
    private String remark;

    @Length(max=100,message = "操作者名长度必须在100字符之内")
    @NotBlank(message = "操作者名不能为空")
    private String operator;
}
