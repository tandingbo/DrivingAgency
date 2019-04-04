package com.beautifulsoup.driving.utils;

import com.beautifulsoup.driving.exception.ParamException;
import com.beautifulsoup.driving.pojo.Agent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

@Slf4j
public class ParamValidatorUtil {
    public static void validateBindingResult(BindingResult result){
        if (result.hasErrors()){
            result.getAllErrors().stream().forEach(error->{
                String errorMsg = error.getDefaultMessage();
                log.error(errorMsg);
                throw new ParamException(error.getDefaultMessage());
            });
        }
    }
    public static void validateContextHolderAgent(Agent agent){
        if (agent == null) {
            throw new ParamException("当前用户信息获取失败");
        }
    }
}
