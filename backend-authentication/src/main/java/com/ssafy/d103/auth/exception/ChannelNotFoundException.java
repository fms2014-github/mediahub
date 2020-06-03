package com.ssafy.d103.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChannelNotFoundException extends RuntimeException{

    private long id;
    private String channelId;

    public ChannelNotFoundException(long id){
        super(String.format("Channel is not found with label Id = %s", id));
        this.id = id;
    }
    public ChannelNotFoundException(String channelId){
        super(String.format("Channel is not found with label channelId = %s", channelId));
        this.channelId = channelId;
    }

    public long getId(){ return id; }
}
