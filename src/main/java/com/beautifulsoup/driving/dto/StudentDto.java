package com.beautifulsoup.driving.dto;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    @Length(max=100,message = "身份证号长度不能超过100字符")
    @JsonProperty(value = "idcard")
    @NotBlank(message = "身份证号不能为空")
    private String studentId;

    @Length(max=100,message = "学生名称长度不能超过100字符")
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;

    @Pattern(regexp = DrivingConstant.Validation.PHONE_REGEX,message = "手机号格式不正确")
    @NotBlank(message = "手机号不能为空")
    private String studentPhone;

    @Length(max=1024,message = "学生照片地址长度不能超过1024字符")
    private String studentImg;

    @Length(max=100,message = "学校名称长度不能超过100字符")
    @NotBlank(message = "学校名称不能为空")
    private String studentSchool;

    private BigDecimal studentPrice;


}
