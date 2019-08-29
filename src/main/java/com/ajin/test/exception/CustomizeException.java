package com.ajin.test.exception;

/**
 * Created by Administrator on 2019/8/15.
 */
public class CustomizeException  extends  RuntimeException{
    private String message;
    private Integer code;

    public CustomizeException(CustomizeErrorCode customizeErrorCode) {
        this.message=customizeErrorCode.getMessage();
        this.code=customizeErrorCode.getCode();


    }
}
