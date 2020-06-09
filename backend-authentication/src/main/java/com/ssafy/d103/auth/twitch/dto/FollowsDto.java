package com.ssafy.d103.auth.twitch.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FollowsDto {
    private String create_at;
    private TwitchChannelDto channel;
    private boolean notifications;
}
