package com.beautifulsoup.driving.common;

import com.beautifulsoup.driving.enums.ResponseCode;
import com.beautifulsoup.driving.exception.AuthenticationException;
import com.beautifulsoup.driving.exception.ParamException;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice(basePackages = {"com.beautifulsoup.driving"})
public class DrivingExceptionSolver {

    @ExceptionHandler(value = ParamException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Map<String,Object>> paramExceptionResolver(ParamException exception){
        Map<String,Object> response= Maps.newConcurrentMap();
        response.put("error",exception.getErrorDescription());
        return ResponseResult.createByError(ResponseCode.ILLEGAL_ARGUMENTS.getCode(),
                ResponseCode.ILLEGAL_ARGUMENTS.getDesc(),response);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseResult<Map<String,Object>> authenticationExceptionHandler(AuthenticationException exception){
        Map<String,Object> response=Maps.newConcurrentMap();
        response.put("error",exception.getErrorMessage());
        return ResponseResult.createByError(ResponseCode.AUTHENTICATION_FAILURE.getCode(),
                ResponseCode.AUTHENTICATION_FAILURE.getDesc(),response);
    }
}
