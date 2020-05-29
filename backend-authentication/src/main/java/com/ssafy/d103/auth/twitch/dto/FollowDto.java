package com.ssafy.d103.auth.twitch.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FollowDto {
    private int _total;
    private FollowDto[] follows;
}
