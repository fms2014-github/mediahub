package com.ssafy.d103.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    private String access_token;
    private String provider;
    private String userId;
}
