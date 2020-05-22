package com.rest.api.controller.v1;

import com.rest.api.advice.exception.CustomEmailSigninFailedException;
import com.rest.api.model.MemberEntity;
import com.rest.api.model.response.SingleResult;
import com.rest.api.service.social.TwitchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"1. Twitch"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/twitch")
public class TwitchController {

    private final TwitchService twitchService;

    @GetMapping(value = "user")
    public SingleResult<String> getUser(@ApiParam(value = "유저 토큰", required = true) @RequestParam String access_token) {
        // twitchService 호출해서 유저 정보 넘기면 된다.
        return null;
    }

}
