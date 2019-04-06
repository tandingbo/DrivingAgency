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
public class CommentDto {
    @ApiModelProperty(value="评论的对象",name="name",example="王小灏",required=true)
    @NotBlank(message = "评论对象名")
    private String name;
    @ApiModelProperty(value="评论内容",name="content",example="这是对王小灏代理的评论,你业绩很好",required=true)
    @Length(max = 1024,message = "评论内容不能超过1024个字符")
    @NotBlank(message = "发布的评论内容不能为空")
    private String content;
}
