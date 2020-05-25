package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.model.MemberEntity;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import com.ssafy.d103.auth.twitch.TwitchService;
import com.ssafy.d103.auth.twitch.dto.ChannelListDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        MemberEntity member = customUserDetailsService.loadMemberById(id);

        System.out.println(member);
//        String twitchUserId = "131655528";
//        List<ChannelListDto> channelList = twitchService.getTwitchAllChannelsByUser(twitchUserId);
//
//        System.out.println(channelList.get(0));
    }
}