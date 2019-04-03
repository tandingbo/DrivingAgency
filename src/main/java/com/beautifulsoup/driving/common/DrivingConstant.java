package com.beautifulsoup.driving.common;

public class DrivingConstant {

    public static final Long TOKEN_EXPIRE=32400000L;//token有效期9小时

    public interface File{
        String UPLOAD_EMPTY_ERROR="上传文件不能为空";
        String UPLOAD_FAILURE="文件上传失败";
    }

    public interface Validation{
        String PHONE_REGEX="^1[34578]\\d{9}$";
        String NUMBER_REGEX="^[0-9]*[1-9][0-9]*$";
        String EMAIL_REGEX="^\\\\w+@(\\\\w+\\\\.){1,2}\\\\w+$";
    }

    public interface Redis{
        String ADMIN_TOKEN="admin_token:";
        String AGENT_TOKEN="agent_token:";
        String ACHIEVEMENT_TOTAL="achievement_total:";
        String ACHIEVEMENT_DAILY="achievement_daily:";
        String ACHIEVEMENT_AGENT="achievement_agent:";
        String LOGIN_AGENTS="login_agents:";
    }
}
