package com.rest.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ADMIN"),
    MEMBER("MEMBER");

    private String value;
}