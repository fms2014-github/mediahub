package com.ssafy.d103.auth.twitch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RetTwitchAuth {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private long expires_in;
    private String[] scope;
}
