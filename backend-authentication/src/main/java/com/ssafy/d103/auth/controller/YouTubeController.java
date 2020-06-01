package com.ssafy.d103.auth.controller;

import com.google.api.services.youtube.YouTube;

import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.google.gson.Gson;
import com.ssafy.d103.auth.commonService.ChannelService;
import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.model.*;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import com.ssafy.d103.auth.security.UserPrincipal;
import com.ssafy.d103.auth.youtube.RetGoogleAuth;
import com.ssafy.d103.auth.youtube.YouTubeDataAPI;
import com.ssafy.d103.auth.youtube.YouTubeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

@Api(tags = {"2. youtube"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/youtube")
public class YouTubeController {
    private final YouTubeService youTubeService;
    private final CustomUserDetailsService customUserDetailsService;
    private final LabelService labelService;
    private final ChannelService channelService;

    @ApiOperation(value = "Google 인증 주소 요청")
    @GetMapping(value = "/token-url")
    public ResponseEntity<?> getCode(){
        return ResponseEntity.ok("\""+youTubeService.getImplicitCodeFlowUrl()+"\"");
    }
    /**
     *
     * Code로 토큰 발급받고, 발급 받은 토큰을 DB에 저장해주는 메소드
     * 메소드 플로우
     * 1. code로 유튜브 토큰 정보 요청
     * 2. 토큰으로 유튜브 유저 정보 받아오기
     * 3. Member 조회
     * 4. RetGoogleAuth로 Auth 객체 생성
     * 5. Member에 Auth 추가
     * 6. customUserDetailService 호출해서 멤버 저장
     *
     * @param code Twitch 인증 코드
     * @param userPrincipal 유저 시큐리티 정보
     * @return Http 상태
     */
    //code로 access token 및 refreshtoken 가져오기
    @ApiOperation(value = "Google 인증 코드로 토큰 발급받고 DB 저장 요청")
    @GetMapping(value = "/token")
    @Transactional
    public ResponseEntity<?> redirectCodeGoogle(@RequestParam String code, @CurrentUser UserPrincipal userPrincipal) {
        RetGoogleAuth retGoogleAuth = youTubeService.getGoogleTokenInfo(code);
        Member member = customUserDetailsService.loadMemberById(userPrincipal.getId());
        Auth auth = new Auth();
        auth.setAuth_provider(AuthProvider.google.toString());
        auth.setAccess_token(retGoogleAuth.getAccess_token());
        auth.setRefresh_token(retGoogleAuth.getRefresh_token());
        auth.setToken_type(retGoogleAuth.getToken_type());
        auth.setMember(member);
        member.getAuth().add(auth);
        customUserDetailsService.saveMember(member);
        return new ResponseEntity(HttpStatus.OK);
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
     * 유튜브 구독 동기화 메소드
     * 1. 동기화 할 유저 조회
     * 2. 구독 리스트 받아오기
     * 3. 회워닝 가지고 있는 루트 라벨에 유튜브 목록을 추가
     * 4. 동기화 이후 first 로그인 값을 올려줌
     * @param userPrincipal
     * @return
     */
    @ApiOperation(value = "유튜브 동기화 요청, 구독 리스트 DB 저장")
    @GetMapping(value = "/synchronization")
    @Transactional
    public ResponseEntity<?> synchronizeWithGoogle(@CurrentUser UserPrincipal userPrincipal) {
        long id = userPrincipal.getId();
        Member member = customUserDetailsService.loadMemberById(id);

        String refreshToken = null;
        for(Auth a : member.getAuth()){
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
                    channel.setLabel(rootLabel);
                    channel.setProvider(AuthProvider.google.toString());
                    channel.setChannelId(item.getSnippet().getResourceId().getChannelId());
                    channel.setProfileImg(item.getSnippet().getThumbnails().getDefault().getUrl());
                    channel.setDescription(item.getSnippet().getDescription());
                    return channel;
                }).collect(Collectors.toList());
        channelService.saveAll(channels);
        member.setFirstLogin(member.getFirstLogin()+1);
        customUserDetailsService.saveMember(member);
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

        SubscriptionListResponse result = youtube
                .subscriptions()
                .list("id, snippet, contentDetails")
                .setMine(true)
                .setMaxResults(maxResult)
                .execute();
        return result;
    }
}