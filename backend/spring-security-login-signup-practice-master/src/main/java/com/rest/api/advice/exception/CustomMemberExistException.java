package com.rest.api.advice.exception;

public class CustomMemberExistException extends RuntimeException {
    public CustomMemberExistException(String msg, Throwable t) {
        super(msg, t);
    }
    public CustomMemberExistException(String msg) {
        super(msg);
    }
    public CustomMemberExistException() {
        super();
    }
}