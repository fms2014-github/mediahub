package com.ssafy.d103.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChannelNotFoundException extends RuntimeException{

    private long id;

    public ChannelNotFoundException(long id){
        super(String.format("Channel is not found with label Id = %s", id));
        this.id = id;
    }

    public long getId(){ return id; }
}
