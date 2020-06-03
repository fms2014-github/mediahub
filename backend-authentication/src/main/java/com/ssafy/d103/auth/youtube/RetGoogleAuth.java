package com.ssafy.d103.auth.youtube;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RetGoogleAuth {
    private String accessToken;
    private String expiresIn;
    private String scope;
    private String tokenType;
    private String idToken;
    private String refreshToken;
}
