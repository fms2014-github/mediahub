package com.ssafy.d103.auth.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.model.AuthProvider;
import com.ssafy.d103.auth.model.Channel;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.model.Member;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import com.ssafy.d103.auth.twitch.TwitchService;
import com.ssafy.d103.auth.twitch.dto.FollowsDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
class TwitchControllerTest {

    @Autowired
    private TwitchService twitchService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @Transactional
    void synchronizeWithTwitch() {
        Long id = 1L;
        Member member = customUserDetailsService.loadMemberById(id);

        System.out.println(member);
        String twitchUserId = "131655528";

        List<FollowsDto> channelList = twitchService.getTwitchAllChannelsByUser(twitchUserId);
        Label rootLabel = labelService.getLabelById(member.getRootLabelId());
        List<Channel> channels = channelList.stream()
                .map(follow -> {
                    Channel channel = new Channel();
                    channel.setProvider(AuthProvider.twitch.toString());
                    channel.setChannelId(follow.getChannel().get_id());
                    channel.setProfileImg(follow.getChannel().getLogo());
                    channel.setFollower(follow.getChannel().getFollowers());
                    channel.setSubscriber(follow.getChannel().getFollowers());
                    channel.setDescription(follow.getChannel().getDescription());
                    return  channel;
                }).collect(Collectors.toList());

        labelService.setChannelsRootLabel(rootLabel, channels);

        rootLabel = labelService.getLabelById(member.getRootLabelId());
        System.out.println("test end");


    }
}