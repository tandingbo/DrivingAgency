package com.beautifulsoup.driving.common;

import com.beautifulsoup.driving.enums.ResponseCode;
import com.beautifulsoup.driving.exception.ParamException;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice(basePackages = {"com.beautifulsoup.driving"})
public class DrivingExceptionSolver {

    @ExceptionHandler(value = ParamException.class)
    public ResponseResult<Map<String,Object>> paramExceptionResolver(ParamException exception){
        Map<String,Object> response= Maps.newConcurrentMap();
        response.put("error",exception.getErrorDescription());
        return ResponseResult.createByError(ResponseCode.ILLEGAL_ARGUMENTS.getCode(),
                ResponseCode.ILLEGAL_ARGUMENTS.getDesc(),response);
    }
}
