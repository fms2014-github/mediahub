package com.ssafy.d103.auth.dto;

import com.ssafy.d103.auth.model.Auth;
import com.ssafy.d103.auth.model.AuthProvider;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.model.RoleType;

import java.util.ArrayList;
import java.util.Collection;

public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private AuthProvider provider;
    private String profileUrl;
    private String providerId;
    private Integer firstLogin;
    private Collection<RoleType> roles = new ArrayList<>();
    private Collection<Auth> auth = new ArrayList<Auth>();
    private Label label;
}
