package com.ssafy.d103.auth.twitch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TwitchUser {
    private String id;
    private String bio;
    private String create_at;
    private String display_name;
    private String email;
    private boolean email_verified;
    private String logo;
    private String name;
    private Notifications notifications;
    private boolean partnered;
    private String twitter_connected;
    private String type;
    private String update_at;

}
