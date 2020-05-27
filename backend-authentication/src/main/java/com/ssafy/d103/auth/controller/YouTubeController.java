package com.ssafy.d103.auth.controller;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.UserPrincipal;
import com.ssafy.d103.auth.youtube.RetGoogleAuth;
import com.ssafy.d103.auth.youtube.YouTubeDataAPI;
import com.ssafy.d103.auth.youtube.YouTubeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Api(tags = {"youtube"})
@RestController
@RequestMapping("/youtube")
public class YouTubeController {
    @Autowired
    YouTubeService youTubeService;

    @GetMapping(value = "/token/code")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCode(@CurrentUser UserPrincipal userPrincipal){
        System.out.println(userPrincipal.getId());
        System.out.println(userPrincipal.getEmail());
        System.out.println(userPrincipal.getName());
        System.out.println(userPrincipal.getAttributes());
        System.out.println(userPrincipal.getAuthorities());
        return ResponseEntity.ok("\""+youTubeService.getImplicitCodeFlowUrl()+"\"");
    }

    @GetMapping(value = "/google/code")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> redirectCodeGoogle(@RequestParam String code) {
        return ResponseEntity.ok(youTubeService.getGoogleTokenInfo(code));
    }

    @GetMapping(value = "/subscriptions/{accessToken}")
    public ResponseEntity<?> getYouTubeSubscriptions(@PathVariable String accessToken){
        System.out.println(accessToken);
        return ResponseEntity.ok("\""+youTubeService.getYouTubeDataAPI(accessToken,"subscriptions")+"\"");
    }

    @GetMapping(value = "/search/{channelId}/{accessToken}")
    public ResponseEntity<?> getYouTubeVideoId(@PathVariable String channelId, @PathVariable String accessToken){
        System.out.println(channelId);
        return ResponseEntity.ok("\""+youTubeService.getYouTubeVideoId(channelId, accessToken,"search")+"\"");
    }

    //////////////youtube api test...
    @GetMapping(value = "/subscriptions/test")
    public List<List<String>> getSubscriptions(@CurrentUser UserPrincipal userPrincipal) throws IOException{
        Long maxResult = 25L;
        List<List<String>> chatMessages = new LinkedList<>();
        YouTube youtube = YouTubeDataAPI.getYouTubeService(userPrincipal);
        SubscriptionListResponse result = youtube
                .subscriptions()
                .list("id, snippet, contentDetails")
                .setMine(true)
                .setMaxResults(maxResult)
                .execute();
        // 구독 정보 가져오기
        System.out.println("test");
        for (Subscription sub : result.getItems()){
            SearchListResponse search = youtube
                    .search()
                    .list("snippet")
                    .setChannelId(sub.getSnippet().getResourceId().getChannelId())
                    .setEventType("live")
                    .setType("video")
                    .execute();
            // 구독 정보에서 live streaming 중인 것 찾기
            if(!search.getItems().isEmpty()) {
                System.out.println("search" + sub.getSnippet().getTitle() + " : " + search);
                System.out.println(search.getItems().size());
                for (int i = 0; i < search.getItems().size(); i++){
                    SearchResult searchResult = search.getItems().get(i);
                    chatMessages.add(new LinkedList<String>());
                    System.out.println(searchResult.getId().getVideoId());
                    VideoListResponse videoListResponse = youtube
                            .videos()
                            .list("liveStreamingDetails")
                            .setId(searchResult.getId().getVideoId())
                            .execute();
                    // streaming 중인것에서 video id 찾기 (한 채널에 2개 이상일 수 있습니다.)
                    chatMessages.add(new LinkedList<>());

                    for (Video v:videoListResponse.getItems()) {
                        LiveChatMessageListResponse liveChatMessageListResponse = youtube
                                .liveChatMessages()
                                .list(v.getLiveStreamingDetails().getActiveLiveChatId(),"snippet")
                                .execute();
                        for (LiveChatMessage liveChatMessage:liveChatMessageListResponse.getItems()){
                            chatMessages.get(i).add(liveChatMessage.getSnippet().getDisplayMessage());
                        }
                    }
                }
            }
        }
        System.out.println(chatMessages);
        return chatMessages;
    }
}