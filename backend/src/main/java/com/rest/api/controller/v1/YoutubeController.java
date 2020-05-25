package com.rest.api.controller.v1;

import com.rest.api.service.MemberService;
import com.rest.api.service.ResponseService;
import com.rest.api.service.social.GoogleService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"1. Google"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/youtube")
public class YoutubeController {
    private final GoogleService googleService;
    private final MemberService memberService;
    private final ResponseService responseService;

    @GetMapping(value = "/{service}/{accesstoken}")
    public String youtubeService(@PathVariable("service") String service,@PathVariable("accesstoken") String accesstoken) {
        return googleService.getYouTubeDataAPI(accesstoken, service);
    }
}
