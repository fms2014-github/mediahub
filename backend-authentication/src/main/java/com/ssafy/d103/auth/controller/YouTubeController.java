package com.ssafy.d103.auth.controller;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;

import com.google.api.services.youtube.model.*;
import com.google.gson.Gson;
import com.ssafy.d103.auth.commonService.AuthService;
import com.ssafy.d103.auth.commonService.ChannelService;
import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.dto.ChannelDto;
import com.ssafy.d103.auth.dto.LabelDto;
import com.ssafy.d103.auth.model.*;
import com.ssafy.d103.auth.model.Channel;
import com.ssafy.d103.auth.model.Member;
import com.ssafy.d103.auth.payload.ChatingMassege;
import com.ssafy.d103.auth.payload.DataChange;
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
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
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
    private final AuthService authService;

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
        Collection<Auth> memberAuth = member.getAuth();
        Iterator<Auth> it = memberAuth.iterator();
        while(it.hasNext()){
            if(it.next().getAuth_provider()=="google")
                return new ResponseEntity(HttpStatus.OK);
        }
        Auth auth = new Auth();
        auth.setAuth_provider(AuthProvider.google.toString());
        auth.setAccess_token(retGoogleAuth.getAccessToken());
        auth.setRefresh_token(retGoogleAuth.getRefreshToken());
        auth.setToken_type(retGoogleAuth.getTokenType());
        auth.setMember(member);
        member.getAuth().add(auth);
        customUserDetailsService.saveMember(member);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/setToken")
    public ResponseEntity<?> redirectCodeGoogle(@RequestBody RetGoogleAuth retGoogleAuth,
                                                @CurrentUser UserPrincipal userPrincipal) {
        System.out.println("===================setToken=======================");
        Member member = customUserDetailsService.loadMemberById(userPrincipal.getId());
        boolean check = false;
        Long authId = 0L;
        Auth auth = new Auth();
        for(Auth a :member.getAuth()){
            if(a.getAuth_provider().equals(AuthProvider.google.toString())){
                check = true;
                authId = a.getId();
            }
        }
        if(check){ // 있을때
            for (Auth a : member.getAuth()){
                if(authId == a.getId()){
                    a.setRefresh_token(retGoogleAuth.getRefreshToken());
                    a.setAccess_token(retGoogleAuth.getAccessToken());
                }
            }
        }else{ // 없을때
            auth.setAuth_provider(AuthProvider.google.toString());
            auth.setAccess_token(retGoogleAuth.getAccessToken());
            auth.setRefresh_token(retGoogleAuth.getRefreshToken());
            auth.setToken_type(retGoogleAuth.getTokenType());
            auth.setMember(member);
            member.getAuth().add(auth);
        }
        System.out.println(retGoogleAuth.getAccessToken());

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
    public ResponseEntity<?> synchronizeWithGoogle(@CurrentUser UserPrincipal userPrincipal) throws IOException {
        long id = userPrincipal.getId();
        Member member = customUserDetailsService.loadMemberById(id);

        String refreshToken = null;
        for(Auth a : member.getAuth()){
            if(a.getAuth_provider().equals("google")){
                refreshToken = a.getRefresh_token();
            }
        }

        YouTube youTube = YouTubeDataAPI.getYouTubeService(refreshToken);
        SubscriptionListResponse subscriptionListResponse = youTube.subscriptions()
            .list("id, snippet, contentDetails")
            .setMaxResults(200L)
            .setMine(true)
            .execute();
        member.getRootLabelId();

        Label rootLabel = labelService.getLabelById(member.getRootLabelId());
        List<Channel> channels = subscriptionListResponse.getItems().stream()
                .map(item -> {
                    Channel channel = new Channel();
                    channel.setLabel(rootLabel);
                    channel.setName(item.getSnippet().getTitle());
                    channel.setDisplayName(item.getSnippet().getTitle());
                    channel.setProvider(AuthProvider.google.toString());
                    channel.setChannelId(item.getSnippet().getResourceId().getChannelId());
                    channel.setProfileImg(item.getSnippet().getThumbnails().getDefault().getUrl());
                    channel.setDescription(item.getSnippet().getDescription());
                    channel.setSubscriptionId(item.getId());
                    return channel;
                }).collect(Collectors.toList());
        List<Channel> memberChannels = new ArrayList<>();
        List<LabelDto> label = new LinkedList<>();
        LinkedList<Label> queue = new LinkedList<>();
        queue.add(rootLabel);

        while(!queue.isEmpty()){
            int size = queue.size();
            for(int i=0; i<size; i++){
                Label temp = queue.poll();
                queue.addAll(temp.getSubLabels());
                label.add(new LabelDto(temp));
                temp.getChannels().forEach(channel -> {
                   memberChannels.add(channel);
                });
            }
        }
        System.out.println("===========syncro before============");
        System.out.println("현 DB 채널"+memberChannels);
        System.out.println("구독중 채널"+channels);
        for (int i = 0; i<memberChannels.size();i++){
            for (int j = 0; j<channels.size(); j++){
                if(memberChannels.get(i).getChannelId().equals(channels.get(j).getChannelId())){
                    memberChannels.remove(i);
                    channels.remove(j);
                    i--;
                    break;
                }
            }
        }
        System.out.println("=======syncro after========");
        System.out.println("없어질 채널 "+memberChannels);
        System.out.println("추가채널"+channels);
        // youtube에서 받은 subscription 정보 중 현 DB와 비교하여 남은 channel들
        if(channels.size()>0)
            channelService.saveAll(channels);
        // 현 DB와 youtube subscription 정보를 비교하여 youtube에 없으나 DB에 남은 정보들
        for (int i=0; i<memberChannels.size();i++){
            System.out.println(memberChannels.get(i).getId() + " 삭제 중 Channel");
            channelService.deleteChannel(memberChannels.get(i).getId());
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping(value = "/search/{channelId}/{accessToken}")
    public ResponseEntity<?> getYouTubeVideoId(@PathVariable String channelId, @PathVariable String accessToken){
        System.out.println(channelId);
        return ResponseEntity.ok("\""+youTubeService.getYouTubeVideoId(channelId, accessToken,"search")+"\"");
    }

    /***
     * 1. channel id로 subscription 추가
     * 2. channel id로 사용자 채널정보 추가
     * @param dataChange
     * @param userPrincipal
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "channelId 요청할시 insert")
    @PostMapping(value = "/subscription")
    public ResponseEntity<?> insertSubscription(@RequestBody DataChange dataChange, @CurrentUser UserPrincipal userPrincipal) throws IOException{
        System.out.println("================구독 시작 추가=================");
        System.out.println(dataChange.getChannelId());
        System.out.println(userPrincipal.getId());
        long id = userPrincipal.getId();
        Member member = customUserDetailsService.loadMemberById(id);
        //youtube
        String refreshToken = null;
        for(Auth a : member.getAuth()){
            if(a.getAuth_provider().equals("google")){
                refreshToken = a.getRefresh_token();
            }
        }
        YouTube youTube = YouTubeDataAPI.getYouTubeService(refreshToken);
        ResourceId resourceId = new ResourceId();
        resourceId.setChannelId(dataChange.getChannelId());
        resourceId.setKind("youtube#channel");
        SubscriptionSnippet snippet = new SubscriptionSnippet();
        snippet.setResourceId(resourceId);
        Subscription subscription = new Subscription();
        subscription.setSnippet(snippet);
        YouTube.Subscriptions.Insert subscriptionInsert =
                youTube.subscriptions().insert("snippet,contentDetails", subscription);
        Subscription returnedSubscription = subscriptionInsert.execute();
        System.out.println("===========구독 추가============");
        System.out.println(returnedSubscription.getSnippet().getTitle());
        System.out.println(dataChange.getChannelId());
        System.out.println(member.getRootLabelId());
        // 채널 추가 로직

        Channel channel = new Channel();
        channel.setChannelId(dataChange.getChannelId());
        channel.setSubscriptionId(returnedSubscription.getId());
        channel.setDisplayName(returnedSubscription.getSnippet().getTitle());
        channel.setName(returnedSubscription.getSnippet().getTitle());
        channel.setDescription(returnedSubscription.getSnippet().getDescription());
        channel.setProfileImg(returnedSubscription.getSnippet().getThumbnails().getDefault().getUrl());
        channel.setProvider(AuthProvider.google.toString());
        channelService.createNewChannel(member.getRootLabelId(),channel);
        System.out.println("===========구독 추가 끝============");
        ChannelDto channelDto = new ChannelDto(
                channel.getId(),
                channel.getLabel().getId(),
                channel.getProvider(),
                channel.getChannelId(),
                channel.getName(),
                channel.getDisplayName(),
                channel.getProfileImg(),
                channel.getFollower(),
                channel.getSubscriber(),
                channel.getDescription()
        );

        return ResponseEntity.ok(channelDto);
    }

    @ApiOperation(value = "channel table의 pk로 요청할시 delete")
    @DeleteMapping(value = "/subscription/{channelPrimaryKey}")
    public ResponseEntity<?> deleteSubscriptions(@PathVariable Long channelPrimaryKey, @CurrentUser UserPrincipal userPrincipal) throws IOException{

        long id = userPrincipal.getId();
        Member member = customUserDetailsService.loadMemberById(id);
        Channel channel = channelService.findById(channelPrimaryKey);

        String refreshToken = null;
        for(Auth a : member.getAuth()){
            if(a.getAuth_provider().equals("google")){
                refreshToken = a.getRefresh_token();
            }
        }
        YouTube youTube = YouTubeDataAPI.getYouTubeService(refreshToken);
        youTube.subscriptions().delete(channel.getSubscriptionId());

        channelService.deleteChannel(channelPrimaryKey);
        return ResponseEntity.ok("");
    }

    @ApiOperation(value = "channelId 요청할시 insert")
    @PostMapping(value = "/chating")
    public ResponseEntity<?> insertChat(@RequestBody ChatingMassege msg, @CurrentUser UserPrincipal userPrincipal) throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        System.out.println(msg);
        System.out.println("=========================채팅 Insert=======================");
        long id = userPrincipal.getId();
        Member member = customUserDetailsService.loadMemberById(id);
        //youtube
        String refreshToken = null;
        for(Auth a : member.getAuth()){
            if(a.getAuth_provider().equals("google")){
                refreshToken = a.getRefresh_token();
            }
        }
        YouTube youTube = YouTubeDataAPI.getYouTubeService(refreshToken);
        LiveChatMessage liveChatMessage = new LiveChatMessage();
        LiveChatMessageSnippet snippet = new LiveChatMessageSnippet();
        snippet.setLiveChatId(msg.getLiveChatId());

        LiveChatTextMessageDetails textMessageDetails = new LiveChatTextMessageDetails();
        textMessageDetails.setMessageText(".");

        snippet.setTextMessageDetails(textMessageDetails);
        snippet.setType("textMessageEvent");

        liveChatMessage.setSnippet(snippet);
        System.out.println(liveChatMessage);
        System.out.println("++++send++++");
        YouTube.LiveChatMessages.Insert request = youTube.liveChatMessages()
                .insert("snippet", liveChatMessage);
        LiveChatMessage response = request.execute();
        System.out.println(response);
        System.out.println("=========================채팅=======================");
        return ResponseEntity.ok(response);
    }
}