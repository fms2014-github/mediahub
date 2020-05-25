package com.ssafy.d103.auth.model;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleType {
    ADMIN("ADMIN"),
    MEMBER("MEMBER");

    private String value;
}