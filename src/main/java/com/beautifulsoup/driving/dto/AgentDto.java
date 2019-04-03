package com.beautifulsoup.driving.dto;

import com.beautifulsoup.driving.common.DrivingConstant;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto {

    @Length(max = 100,message = "代理人的名字必须在100字符之内")
    @NotBlank(message = "代理名不能为空")
    private String agentName;

    @Length(max=50,message = "密码长度必须在50个字符以内")
    @NotBlank(message = "密码不能为空")
    private String agentPassword;

    @Pattern(regexp = DrivingConstant.Validation.PHONE_REGEX,message = "手机号格式不正确")
    @NotBlank(message = "手机号不能为空")
    private String agentPhone;

    @Length(max = 50,message = "邮箱长度必须在50个字符以内")
    @NotBlank(message = "邮箱不能为空")
    private String agentEmail;

    @Length(max = 100,message = "身份证号长度过长")
    @NotBlank(message = "身份证号不能为空")
    private String agentIdcard;

    @Length(max = 1024,message = "身份证照片地址长度过长")
    private String agentIdcardImg;

    @Length(max = 100,message = "学校名称长度太长")
    private String agentSchool;

    private Integer parentId;

    private Integer status;
}
