package com.beautifulsoup.driving.utils;

import com.beautifulsoup.driving.common.ResponseResult;
import com.beautifulsoup.driving.enums.ResponseCode;

import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ResponseUtil {
    public static void errorAuthentication(HttpServletResponse response, String errorMsg) {
        PrintWriter out=null;
        try {
            Map<String,Object> data= Maps.newConcurrentMap();
            data.put("error",errorMsg);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            ResponseResult responseResult= ResponseResult.createByError(ResponseCode.AUTHENTICATION_FAILURE.getCode()
                    ,ResponseCode.AUTHENTICATION_FAILURE.getDesc(),data);
            out=response.getWriter() ;
            out.write(JsonSerializerUtil.obj2String(responseResult));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }


    }
    public static void warningAuthentication(HttpServletResponse response, String warningMsg) {
        PrintWriter out=null;
        try {
            Map<String,Object> data= Maps.newConcurrentMap();
            data.put("error",warningMsg);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            ResponseResult responseResult= ResponseResult.createByError(ResponseCode.AUTHENTICATION_FAILURE.getCode()
                    ,ResponseCode.AUTHENTICATION_FAILURE.getDesc(),data);
            out=response.getWriter() ;
            out.write(JsonSerializerUtil.obj2String(responseResult));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }

    }
}
