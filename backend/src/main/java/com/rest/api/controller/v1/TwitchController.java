package com.rest.api.controller.v1;

import com.rest.api.advice.exception.CustomEmailSigninFailedException;
import com.rest.api.config.security.JwtTokenProvider;
import com.rest.api.model.MemberEntity;
import com.rest.api.model.response.SingleResult;
import com.rest.api.model.social.twitch.RetTwitchAuth;
import com.rest.api.service.MemberService;
import com.rest.api.service.ResponseService;
import com.rest.api.service.social.TwitchService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Api(tags = {"1. Twitch"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/twitch")
public class TwitchController {

    private final TwitchService twitchService;
    private final MemberService memberService;
    private final ResponseService responseService;

    @GetMapping(value = "user")
    public SingleResult<String> getUser(@ApiParam(value = "유저 토큰", required = true) @RequestParam String access_token) {
        // twitchService 호출해서 유저 정보 넘기면 된다.
        return null;
    }

    @GetMapping(value = "/token-url")
    public SingleResult<String> redirectTwitch() {
        return responseService.getSingleResult(twitchService.getImplicitCodeFlowUrl());
    }

    @GetMapping(value = "/token/code")
    public SingleResult<String> redirectCodeTwitch(@RequestParam String code) {
        RetTwitchAuth rta = twitchService.getTwitchTokenInfo(code);
        return responseService.getSingleResult(rta.getAccess_token());
    }

    @GetMapping(value = "/synchronization")
    public SingleResult<String> synchronizeWithTwitch() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwt = request.getHeader("Authorization");




        return null;
    }

}
