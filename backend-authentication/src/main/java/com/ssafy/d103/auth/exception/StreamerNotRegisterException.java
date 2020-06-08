package com.ssafy.d103.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StreamerNotRegisterException extends RuntimeException{
    private String id;

    public StreamerNotRegisterException(String id){
        super(String.format("Streamer doesn't register with channel = %s", id));
    }

    public String getId(){
        return id;
    }
}
