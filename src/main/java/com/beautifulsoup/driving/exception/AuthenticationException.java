package com.beautifulsoup.driving.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private String errorMessage;
    public AuthenticationException(String message) {
        this.errorMessage=message;
    }
}
