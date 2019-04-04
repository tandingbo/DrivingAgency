package com.beautifulsoup.driving.dto;

import com.beautifulsoup.driving.common.DrivingConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="代理信息",description="代理注册信息")
public class AgentDto {

    @ApiModelProperty(value="代理名",name="agentName",example="BeautifulSoup",required=true)
    @Length(max = 100,message = "代理人的名字必须在100字符之内")
    @NotBlank(message = "代理名不能为空")
    private String agentName;

    @ApiModelProperty(value="手机号",name="agentPhone",example="17864195311",required=true)
    @Pattern(regexp = DrivingConstant.Validation.PHONE_REGEX,message = "手机号格式不正确")
    @NotBlank(message = "手机号不能为空")
    private String agentPhone;

    @ApiModelProperty(value="邮箱地址",name="agentEmail",example="beautifulsoup@163.com",required=true)
    @Length(max = 50,message = "邮箱长度必须在50个字符以内")
    @NotBlank(message = "邮箱不能为空")
    private String agentEmail;

    @ApiModelProperty(value="身份证号",name="agentIdcard",example="372330199700000000",required=true)
    @Length(max = 100,message = "身份证号长度过长")
    @NotBlank(message = "身份证号不能为空")
    private String agentIdcard;

    @ApiModelProperty(value="身份证照片地址",name="agentIdcardImg",example="http://127.0.0.1:8888/driving/M00/00/00/rBEZLVyjXheAXQ3NAAKNiCjQX7k807.png",required=true)
    @Length(max = 1024,message = "身份证照片地址长度过长")
    private String agentIdcardImg;

    @ApiModelProperty(value="代理人的学校",name="agentSchool",example="北京大学",required=true)
    @Length(max = 100,message = "学校名称长度太长")
    private String agentSchool;
}
