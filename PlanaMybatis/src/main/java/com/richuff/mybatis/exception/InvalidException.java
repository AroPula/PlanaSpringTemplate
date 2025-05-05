package com.richuff.mybatis.exception;

public class InvalidException extends Exception{
    public InvalidException(){}
    public InvalidException(String message){
        super(message);
    }
}
