package com.ssafy.d103.auth.twitch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notifications {
    private boolean email;
    private boolean push;
}
