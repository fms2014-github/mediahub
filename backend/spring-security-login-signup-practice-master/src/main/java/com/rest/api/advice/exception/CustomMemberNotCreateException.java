package com.rest.api.advice.exception;

public class CustomMemberNotCreateException extends  RuntimeException{
    public CustomMemberNotCreateException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomMemberNotCreateException(String msg) {
        super(msg);
    }

    public CustomMemberNotCreateException() {
        super();
    }
}
