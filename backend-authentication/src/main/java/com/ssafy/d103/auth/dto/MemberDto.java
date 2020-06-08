package com.ssafy.d103.auth.dto;

import com.ssafy.d103.auth.model.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<LabelDto> label;

    public MemberDto(Member member, List<LabelDto> label){
        this.name = member.getName();
        this.email = member.getEmail();
        this.provider = member.getProvider();
        this.profileUrl = member.getProfileUrl();
        this.providerId = member.getProviderId();
        this.firstLogin = member.getFirstLogin();
        this.roles = member.getRole();
        this.auth = member.getAuth().stream().map(item -> {
            AuthDto a = new AuthDto();
            a.setAccess_token(item.getAccess_token());
            a.setProvider(item.getAuth_provider());
            a.setUserId(Integer.toString(item.getUserId()));
            return a;
        }).collect(Collectors.toList());
        this.label = label;
    }
}
