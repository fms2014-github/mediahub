package com.rest.api.advice.exception;

public class CustomMemberNotFoundException extends  RuntimeException {
    public CustomMemberNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomMemberNotFoundException(String msg) {
        super(msg);
    }

    public CustomMemberNotFoundException() {
        super();
    }
}
