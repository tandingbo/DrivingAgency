package com.beautifulsoup.driving.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgentStatus {
    UNEXAMINED(0,"未审核"),
    EXAMINED(1,"已经审核过"),

    ;
    private final Integer code;
    private final String desc;
}
