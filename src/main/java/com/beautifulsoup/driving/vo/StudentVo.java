package com.beautifulsoup.driving.vo;

import com.beautifulsoup.driving.common.Date2LongSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentVo implements Serializable {
    private static final long serialVersionUID = 2240362381552657272L;
    private Integer id;

    private String studentId;

    private String studentName;

    private String studentPhone;

    private String studentImg;

    private String studentSchool;

    private BigDecimal studentPrice;

    private String operator;

    @JsonProperty(value = "addTime")
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

}
