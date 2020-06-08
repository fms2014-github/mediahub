package com.ssafy.d103.auth.dto;

import com.ssafy.d103.auth.model.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {
    private Long id;
    private Long labelId;
    private String provider;
    private String channelId;
    private String name;
    private String displayName;
    private String profileImg;
    private Long follower;
    private Long subscriber;
    private String description;

    public ChannelDto(Channel channel){
        this.id = channel.getId();
        this.labelId = channel.getLabel().getId();
        this.provider = channel.getProvider();
        this.channelId = channel.getChannelId();
        this.name = channel.getName();
        this.displayName = channel.getDisplayName();
        this.profileImg = channel.getProfileImg();
        this.follower = channel.getFollower();
        this.subscriber = channel.getSubscriber();
        this.description = channel.getDescription();
    }
}
