package com.ssafy.d103.auth.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatingMassege {
    String liveChatId;
    String messageText;
}
