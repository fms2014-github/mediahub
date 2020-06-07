package com.ssafy.d103.auth.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "multi_platform")
public class StreamerMultiPlatform {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "youtube_channel_id")
    String youTubeChannelId;
    @Column(name = "twitch_id")
    String twitchId;
}
