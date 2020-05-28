package com.ssafy.d103.auth.twitch.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChannelDto {
    private String display_name;
    private String _id;
    private String name;
    private String logo;
    private String video_banner;
    private String profile_banner;
    private String url;
    private String description;
    private Long followers;
}
