package com.beautifulsoup.driving.common;

public class DrivingConstant {

    public static final Long TOKEN_EXPIRE=7200000L;//token有效期2小时
    public static final Long REFRESH_TOKEN_EXPIRE=604800L;//refresh token有效期1周

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
        String LOGIN_AGENTS="login_agents:";
        String ADMIN_TOKEN="admin_token:";
        String AGENT_TOKEN="agent_token:";
        String TOKEN_SECRETS="token_secrets:";
        String TOKEN_SECRET="token_secret:";
        String TOKEN_BLACKLIST="token_blacklist:";
        String TOKEN_REFRESH="token_refresh:";
        String TOKEN_INVALID="token_invalid:";

        String ACHIEVEMENT_TOTAL="achievement_total:";
        String ACHIEVEMENT_DAILY="achievement_daily:";
        String ACHIEVEMENT_AGENT="achievement_agent:";

    }
}
