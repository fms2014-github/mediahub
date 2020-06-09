package com.ssafy.d103.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreamChannelDto {
    private String provider;
    private String channelId;
    private String twitchChannelId;
}
