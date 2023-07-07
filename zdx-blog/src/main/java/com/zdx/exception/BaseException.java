package com.zdx.exception;

import com.zdx.utils.MessageUtil;


public class BaseException extends RuntimeException{

    private final String message;

    public BaseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return MessageUtil.getLocaleMessage(message);
    }
}
