package com.ssafy.d103.auth.controller;

import com.google.api.services.youtube.model.*;
import com.ssafy.d103.auth.youtube.YouTubeDataAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags = {"youtube"})
@RestController
@RequestMapping("/youtube")
public class YouTubeRestController {
    @GetMapping(value = "/test")
    public String test(){
        return "test";
    }
    @GetMapping(value = "/api/channels/{channelId}")
    public ChannelListResponse getChannels(@PathVariable("channelId") String channelId) throws IOException {

        ChannelListResponse result = YouTubeDataAPI.getYouTubeService()
                .channels()
                .list("id,snippet,brandingSettings,contentDetails,invideoPromotion,statistics,topicDetails")
                .setId(channelId)
                .execute();

        return result;
    }
    @GetMapping(value = "/api/subscriptions")
    public SubscriptionListResponse getSubscriptions() throws IOException {
        Long maxResult = 100L;
        SubscriptionListResponse result = YouTubeDataAPI.getYouTubeService()
                .subscriptions()
                .list("id, snippet, contentDetails")
                .setMine(true)
                .setMaxResults(maxResult)
                .execute();
        System.out.println("subscription list : " + result.getItems());
        return result;
    }
    @GetMapping(value = "/api/playlists")
    public PlaylistListResponse getPlaylists() throws IOException {
        Long maxResult = 100L;
        PlaylistListResponse result = YouTubeDataAPI.getYouTubeService()
                .playlists()
                .list("id, snippet, status")
                .setMine(true)
                .execute();
        return result;
    }
    @GetMapping(value = "/api/liveBroadcasts")
    public LiveBroadcastListResponse getLiveBroadcasts() throws IOException {
        LiveBroadcastListResponse result = YouTubeDataAPI.getYouTubeService()
                .liveBroadcasts()
                .list("snippet")
                .setId("VFhkX2GesZE")
                .execute();
        return result;
    }
    @GetMapping(value = "/api/liveMessages")
    public LiveChatMessageListResponse getLiveMessages() throws IOException {
        LiveChatMessageListResponse result = YouTubeDataAPI.getYouTubeService()
                .liveChatMessages()
                .list("Cg0KC1ZGaGtYMkdlc1pFKicKGFVDcW1yUEQyZC01WFJOZHJLLWFOSWF2ZxILVkZoa1gyR2VzWkU","snippet")
                .execute();
        return result;
    }
    @GetMapping(value = "/api/search/{channelId}")
    public SearchListResponse getSearch(@PathVariable("channelId") String channelId) throws IOException {
        SearchListResponse result = YouTubeDataAPI.getYouTubeService()
                .search()
                .list("snippet")
                .setChannelId(channelId)
                .setEventType("live")
                .setType("video")
                .execute();
        return result;
    }
}