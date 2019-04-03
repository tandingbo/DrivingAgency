package com.beautifulsoup.driving.dto;

import com.beautifulsoup.driving.common.DrivingConstant;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class StudentDto {
    @Length(max=100,message = "学号长度不能超过100字符")
    @NotBlank(message = "学生学号不能为空")
    private String studentId;

    @Length(max=100,message = "学生名称长度不能超过100字符")
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;

    @Pattern(regexp = DrivingConstant.Validation.PHONE_REGEX,message = "手机号格式不正确")
    @NotBlank(message = "手机号不能为空")
    private String studentPhone;

    @Length(max=1024,message = "学生照片地址长度不能超过1024字符")
    @NotBlank(message = "学生照片地址不能为空")
    private String studentImg;

    @Length(max=100,message = "学校名称长度不能超过100字符")
    @NotBlank(message = "学校名称不能为空")
    private String studentSchool;

    private BigDecimal studentPrice;

    @Length(max=100,message = "操作者名长度必须在100字符之内")
    @NotBlank(message = "操作者名不能为空")
    private String operator;

    private Integer status;

}
