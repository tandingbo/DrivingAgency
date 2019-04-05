package com.beautifulsoup.driving.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudentStatus {
    AVAILABLE(1,"学员信息已审核"),
    UNAVAILABLE(0,"学员信息未审核")
    ;
    private final Integer status;

    private final String desc;
}
