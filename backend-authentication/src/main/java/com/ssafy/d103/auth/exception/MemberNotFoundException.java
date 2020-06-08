package com.ssafy.d103.auth.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(long id){
        super(String.format("member %s is not found :: ", id));
    }
}
