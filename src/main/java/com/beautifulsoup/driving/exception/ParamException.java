package com.beautifulsoup.driving.exception;

import lombok.Getter;

@Getter
public class ParamException extends RuntimeException{

    private String errorMessage;
    private String errorDescription;
    private int code;

    public ParamException(String message) {
        this.errorMessage=message;
    }

    public ParamException(String message,String description) {
        this.errorMessage=message;
        this.errorDescription=description;
    }
    public ParamException(String message,String description,int code) {
        this.errorMessage=message;
        this.errorDescription=description;
        this.code=code;
    }
}

