package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.model.MemberEntity;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import com.ssafy.d103.auth.security.TokenProvider;
import com.ssafy.d103.auth.security.UserPrincipal;
import com.ssafy.d103.auth.twitch.TwitchService;
import com.ssafy.d103.auth.twitch.dto.ChannelListDto;
import com.ssafy.d103.auth.twitch.model.RetTwitchAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@Api(tags = {"1. Twitch"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/twitch")
public class TwitchController {

    private final TwitchService twitchService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping(value = "/user")
    public ResponseEntity<?> getUser(@ApiParam(value = "유저 토큰", required = true) @RequestParam String access_token) {
        // twitchService 호출해서 유저 정보 넘기면 된다.
        return null;
    }

    @GetMapping(value = "/token-url")
    public ResponseEntity<?> redirectTwitch() {
        return ResponseEntity.ok("\""+twitchService.getImplicitCodeFlowUrl()+"\"");
    }

    @GetMapping(value = "/token/code")
    public ResponseEntity<?> redirectCodeTwitch(@RequestParam String code) {
        return ResponseEntity.ok("\""+twitchService.getTwitchTokenInfo(code).getAccess_token()+"\"");
    }

    /**
     * 트위치 구독 동기화 메소드
     * 1. 동기화할 유저 조회
     * 2. 구독 리스트 받아옴
     *
     * @param
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @GetMapping(value = "/synchronization")
    public ResponseEntity<?> synchronizeWithTwitch(@CurrentUser UserPrincipal userPrincipal) {
        long id = userPrincipal.getId();
        MemberEntity member = customUserDetailsService.loadMemberById(id);
        String twitchUserId = (String) userPrincipal.getAttributes().get("twitchUserId");
        List<ChannelListDto> channelList = twitchService.getTwitchAllChannelsByUser(twitchUserId);

        /*
        DB에 access token , refresh token 발급한 상태
        1. userid로 채널 불러옴
         */

        return null;
    }

}