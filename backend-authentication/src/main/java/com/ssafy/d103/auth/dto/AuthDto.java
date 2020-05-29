package com.ssafy.d103.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class AuthDto {
    private String access_token;
    private String provider;
}
