package com.ssafy.d103.auth.controller;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.gson.Gson;
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

    //code 가져오기
    @GetMapping(value = "/token/code")
    public ResponseEntity<?> getCode(@CurrentUser UserPrincipal userPrincipal){
        System.out.println(userPrincipal.getId());
        System.out.println(userPrincipal.getEmail());
        System.out.println(userPrincipal.getName());
        System.out.println(userPrincipal.getAttributes());
        System.out.println(userPrincipal.getAuthorities());
        return ResponseEntity.ok("\""+youTubeService.getImplicitCodeFlowUrl()+"\"");
    }

    //code로 access token 및 refreshtoken 가져오기
    @GetMapping(value = "/google/code")
    public ResponseEntity<?> redirectCodeGoogle(@RequestParam String code) {
        return ResponseEntity.ok(youTubeService.getGoogleTokenInfo(code));
    }

    //refreshtoken으로 access token 갱신
    @PostMapping(value = "/google/refreshing")
    public ResponseEntity<?> refreshingGoogleAccessToken(@RequestBody String refreshToken) {
        System.out.println(refreshToken);
        return ResponseEntity.ok(youTubeService.getRefreshingAccessToken(refreshToken));
    }

    //accessToken으로 subscriptions 가지고 오기
    @GetMapping(value = "/subscriptions/{accessToken}")
    public ResponseEntity<?> getYouTubeSubscriptions(@PathVariable String accessToken){
        return ResponseEntity.ok("\""+youTubeService.getYouTubeSubscriptions(accessToken,"subscriptions")+"\"");
    }

    @GetMapping(value = "/search/{channelId}/{accessToken}")
    public ResponseEntity<?> getYouTubeVideoId(@PathVariable String channelId, @PathVariable String accessToken){
        System.out.println(channelId);
        return ResponseEntity.ok("\""+youTubeService.getYouTubeVideoId(channelId, accessToken,"search")+"\"");
    }

    /*
    * youtube api test codes
    * 사용자 채팅을 들고올수 있습니다.
    * */
    @GetMapping(value = "/channelLive/{channelId}")
    public SubscriptionListResponse getSubscriptions(@PathVariable String channelId) throws IOException{
        Long maxResult = 25L;
        System.out.println(channelId);
        YouTube youtube = YouTubeDataAPI.getYouTubeService("1//0eQl_D7lsBlB-CgYIARAAGA4SNwF-L9IrLBrFg45fli-5IVkShXDzW1fXU3GWeKeEJAqcCnvHGoeyaPz8jb_J1ezOK5dbUNN1iTU");
        //
        SubscriptionListResponse result = youtube
                .subscriptions()
                .list("id, snippet, contentDetails")
                .setMine(true)
                .setMaxResults(maxResult)
                .execute();
//        SearchListResponse search = youtube.search()
//                .list("snippet")
//                .setChannelId("UCX4sShAQf01LYjYQhG2ZgKg")
//                .setType("video")
//                .execute();
        return result;
    }
}