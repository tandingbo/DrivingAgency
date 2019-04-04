package com.beautifulsoup.driving.dto;

import com.beautifulsoup.driving.common.DrivingConstant;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentNewDto {

    @Pattern(regexp = DrivingConstant.Validation.PHONE_REGEX,message = "手机号格式不正确")
    private String agentPhone;

    @Length(max = 50,message = "邮箱长度必须在50个字符以内")
    private String agentEmail;

    @Length(max = 1024,message = "身份证照片地址长度过长")
    private String agentIdcardImg;

    @Length(max = 100,message = "学校名称长度太长")
    private String agentSchool;

}
