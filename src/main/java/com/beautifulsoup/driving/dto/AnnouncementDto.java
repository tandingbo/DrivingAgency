package com.beautifulsoup.driving.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDto {

    @ApiModelProperty(value="公告内容",name="content",example="这是新发布的一条公告",required=true)
    @Length(max = 5000,message = "公告内容不能超过5000个字符")
    @NotBlank(message = "发布的公告不能为空")
    private String content;

}
