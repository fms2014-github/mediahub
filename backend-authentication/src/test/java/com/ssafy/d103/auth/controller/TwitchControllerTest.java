package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.model.Member;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import com.ssafy.d103.auth.twitch.TwitchService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class TwitchControllerTest {

    @Autowired
    private TwitchService twitchService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void synchronizeWithTwitch() {
        Long id = 1L;
        Member member = customUserDetailsService.loadMemberById(id);

        System.out.println(member);
//        String twitchUserId = "131655528";
//        List<ChannelListDto> channelList = twitchService.getTwitchAllChannelsByUser(twitchUserId);
//
//        System.out.println(channelList.get(0));
    }
}