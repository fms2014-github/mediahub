package com.ssafy.d103.auth.controller;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.ssafy.d103.auth.youtube.YouTubeDataAPI;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags = {"3. test"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/test")
public class TestController {
    @GetMapping(value = "/")
    public SubscriptionListResponse getYoutube() throws IOException {
        Long maxResult = 25L;
        YouTube youtube = YouTubeDataAPI.getYouTubeService("");
        System.out.println("test");
        SubscriptionListResponse result = youtube
                .subscriptions()
                .list("id, snippet, contentDetails")
                .setMine(true)
                .setMaxResults(maxResult)
                .execute();
        return result;
    }
}
