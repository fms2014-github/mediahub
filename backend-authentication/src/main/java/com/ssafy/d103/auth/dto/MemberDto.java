package com.ssafy.d103.auth.dto;

import com.ssafy.d103.auth.model.Auth;
import com.ssafy.d103.auth.model.AuthProvider;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.model.RoleType;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String name;
    private String email;
    private AuthProvider provider;
    private String profileUrl;
    private String providerId;
    private Integer firstLogin;
    private RoleType roles;
    private Collection<AuthDto> auth;
    private Label label;
}
