package com.ssafy.d103.auth.controller;

import com.google.api.services.youtube.YouTube;

import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.google.gson.Gson;
import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.model.*;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import com.ssafy.d103.auth.security.UserPrincipal;
import com.ssafy.d103.auth.youtube.RetGoogleAuth;
import com.ssafy.d103.auth.youtube.YouTubeDataAPI;
import com.ssafy.d103.auth.youtube.YouTubeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"youtube"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/youtube")
public class YouTubeController {
    private final YouTubeService youTubeService;
    private final CustomUserDetailsService customUserDetailsService;
    private final LabelService labelService;

    //code 가져오기
    @GetMapping(value = "/token-url")
    public ResponseEntity<?> getCode(@CurrentUser UserPrincipal userPrincipal){
        return ResponseEntity.ok("\""+youTubeService.getImplicitCodeFlowUrl()+"\"");
    }

    //code로 access token 및 refreshtoken 가져오기
    @GetMapping(value = "/token-code")
    public ResponseEntity<?> redirectCodeGoogle(@RequestParam String code) {
        RetGoogleAuth retGoogleAuth = youTubeService.getGoogleTokenInfo(code);
        Member member = customUserDetailsService.loadMemberById(3L);
        Auth auth = new Auth();
        auth.setAuth_provider(AuthProvider.google.toString());
        auth.setAccess_token(retGoogleAuth.getAccess_token());
        auth.setRefresh_token(retGoogleAuth.getRefresh_token());
        auth.setToken_type(retGoogleAuth.getToken_type());
        auth.setMember(member);
        member.getAuth().add(auth);
        customUserDetailsService.saveMember(member);
        return ResponseEntity.ok("{}");
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

    /***
     *
     * @param userPrincipal
     * @return
     */
    @ApiOperation(value = "유튜브 동기화 요청, 구독 리스트 DB 저장")
    @GetMapping(value = "/synchronization")
    @Transactional
    public ResponseEntity<?> synchronizeWithGoogle(@CurrentUser UserPrincipal userPrincipal) {
        long id = userPrincipal.getId();
        Member member = customUserDetailsService.loadMemberById(id);

        Auth[] auths = null;
        member.getAuth().toArray(auths);
        String refreshToken = null;
        for(Auth a : auths){
            if(a.getAuth_provider().equals("google")){
                refreshToken = a.getRefresh_token();
            }
        }
        YouTube youTube = YouTubeDataAPI.getYouTubeService(refreshToken);
        SubscriptionListResponse subscriptionListResponse = null;
        try{
            subscriptionListResponse = youTube.subscriptions()
                .list("id, snippet, contentDetails")
                .setMine(true)
                .execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        Label rootLabel = labelService.getLabelById(member.getRootLabelId());
        List<Channel> channels = subscriptionListResponse.getItems().stream()
                .map(item -> {
                    Channel channel = new Channel();
                    channel.setProvider(AuthProvider.google.toString());
                    channel.setChannelId(item.getSnippet().getResourceId().getChannelId());
                    channel.setProfileImg(item.getSnippet().getThumbnails().getDefault().getUrl());
                    channel.setDescription(item.getSnippet().getDescription());
                    return channel;
                }).collect(Collectors.toList());
        labelService.setChannelsRootLabel(rootLabel, channels);
        return new ResponseEntity(HttpStatus.OK);
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