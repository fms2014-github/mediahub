package com.rest.api.model.social.twitch;

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
