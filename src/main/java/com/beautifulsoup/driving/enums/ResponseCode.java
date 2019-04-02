package com.beautifulsoup.driving.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(10001,"请求成功"),
    ERROR(50000,"请求失败"),
    ILLEGAL_ARGUMENTS(50001,"请求参数不合法")
    ;

    private final Integer code;
    private final String desc;
}
