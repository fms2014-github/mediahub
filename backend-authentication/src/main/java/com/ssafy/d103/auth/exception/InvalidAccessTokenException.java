package com.ssafy.d103.auth.exception;

public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException(){
        super("Invalid Access Token Exception, Please Refreshing your Token");
    }
}
