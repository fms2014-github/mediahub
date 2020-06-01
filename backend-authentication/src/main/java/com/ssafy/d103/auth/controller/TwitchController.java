package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.commonService.ChannelService;
import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.commonService.MemberService;
import com.ssafy.d103.auth.model.*;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import com.ssafy.d103.auth.security.UserPrincipal;
import com.ssafy.d103.auth.twitch.TwitchService;
import com.ssafy.d103.auth.twitch.dto.FollowsDto;
import com.ssafy.d103.auth.twitch.model.RetTwitchAuth;
import com.ssafy.d103.auth.twitch.model.TwitchUser;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@Api(tags = {"1. Twitch"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/twitch")
public class TwitchController {
    private final TwitchService twitchService;
    private final CustomUserDetailsService customUserDetailsService;
    private final MemberService memberService;
    private final LabelService labelService;
    private final ChannelService channelService;

    @ApiOperation(value = "Twitch 인증 주소 요청")
    @GetMapping(value = "/token-url")
    public ResponseEntity<?> redirectTwitch() {
        return new ResponseEntity(twitchService.getImplicitCodeFlowUrl(), HttpStatus.OK);
    }

    /**
     *
     * Code로 토큰 발급받고, 발급 받은 토큰을 DB에 저장해주는 메소드
     * 메소드 플로우
     * 1. code로 트위치 토큰 정보 요청
     * 2. 토큰으로 트위치 유저 정보 받아오기
     * 3. Member 조회
     * 4. RetTwitchAuth와 TwitchUser로 Auth 객체 생성
     * 5. Member에 Auth 추가
     * 6. customUserDetailService 호출해서 멤버 저장
     *
     * @param code Twitch 인증 코드
     * @param userPrincipal 유저 시큐리티 정보
     * @return Http 상태
     */
    @ApiOperation(value = "Twitch 인증 코드로 토큰 발급받고 DB 저장 요청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "Twitch Code", required = true)
    })
    @GetMapping(value = "/token-code")
    @Transactional
    public ResponseEntity<?> redirectCodeTwitch(@RequestParam String code, @CurrentUser UserPrincipal userPrincipal) {
        /*
        DB에 저장해야함.
        1. 트위치 토큰 받아오기
        2. 토큰으로 유저 정보 받아오기
        3. Twitch User id랑 같이 Auth 저장하기
         */
        RetTwitchAuth retTwitchAuth = twitchService.getTwitchTokenInfo(code);
        TwitchUser twitchUser = twitchService.getTwitchUserInfo(retTwitchAuth.getAccess_token());
        Member member = customUserDetailsService.loadMemberById(userPrincipal.getId());
        Auth auth = new Auth();
        auth.setAuth_provider(AuthProvider.twitch.toString());
        auth.setAccess_token(retTwitchAuth.getAccess_token());
        auth.setRefresh_token(retTwitchAuth.getRefresh_token());
        auth.setToken_type(retTwitchAuth.getToken_type());
        auth.setUserId(Integer.parseInt(twitchUser.getId()));
        auth.setMember(member);
        member.getAuth().add(auth);
        customUserDetailsService.saveMember(member);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 트위치 구독 동기화 메소드
     * 1. 동기화할 유저 조회
     * 2. 구독 리스트 받아옴
     * 3. 회원이 가지고 있는 루트 라벨에 트위치 목록을 추가해줌
     * 4. 동기화 이후에는 first 로그인 값을 올려줌
     * @param
     * @return
     */
    @ApiOperation(value = "트위치 동기화 요청, 팔로우 리스트 DB 저장")
    @GetMapping(value = "/synchronization")
    @Transactional
    public ResponseEntity<?> synchronizeWithTwitch(@CurrentUser UserPrincipal userPrincipal){
        long id = userPrincipal.getId();
        Member member = customUserDetailsService.loadMemberById(id);
        String twitchUserId = null;
        String accessToken = null;

        for(Auth a : member.getAuth()){
            if(a.getAuth_provider().equals("twitch")){
                twitchUserId = Long.toString(a.getUserId());
                accessToken = a.getAccess_token();
            }
        }

        List<FollowsDto> channelList = twitchService.getTwitchAllChannelsByUser(twitchUserId);
        Label rootLabel = labelService.getLabelById(member.getRootLabelId());
        List<Channel> channels = channelList.stream()
                .map(follow -> {
                    Channel channel = new Channel();
                    channel.setLabel(rootLabel);
                    channel.setProvider(AuthProvider.twitch.toString());
                    channel.setChannelId(follow.getChannel().get_id());
                    channel.setName(follow.getChannel().getName());
                    channel.setDescription(follow.getChannel().getDisplay_name());
                    channel.setProfileImg(follow.getChannel().getLogo());
                    channel.setFollower(follow.getChannel().getFollowers());
                    channel.setSubscriber(follow.getChannel().getFollowers());
                    channel.setDescription(follow.getChannel().getDescription());
                    return channel;
                }).collect(Collectors.toList());

        channelService.saveAll(channels);
        member.setFirstLogin(member.getFirstLogin()+1);
        TwitchUser twitchUser = twitchService.getTwitchUserInfo(accessToken);
        StreamChannel streamChannel = new StreamChannel();
        streamChannel.setId("Y".concat(twitchUser.getName()));
        streamChannel.setMember(member);
        customUserDetailsService.saveMember(member);

        return new ResponseEntity(HttpStatus.OK);
    }

}