package com.rest.api.model.social.twitch;

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
