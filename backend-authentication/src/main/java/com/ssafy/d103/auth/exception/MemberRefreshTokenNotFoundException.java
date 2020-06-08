package com.ssafy.d103.auth.exception;

public class MemberRefreshTokenNotFoundException extends RuntimeException{
    public MemberRefreshTokenNotFoundException(long userId){
        super(String.format("this member's refresh token :: who is %s", userId));
    }
}
