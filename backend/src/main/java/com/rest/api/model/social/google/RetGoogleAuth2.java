package com.rest.api.model.social.google;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RetGoogleAuth2 {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
