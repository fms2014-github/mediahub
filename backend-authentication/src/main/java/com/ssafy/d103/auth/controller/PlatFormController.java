package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.payload.LoginRequest;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.UserPrincipal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"multiplatform"})
@RestController
@RequestMapping("/multiplatform")
public class PlatFormController {
    @Autowired
    private StreamerMultiPlatformRepository streamerMultiPlatformRepository;

    @PostMapping("/login")
    public ResponseEntity<?> platform(@Valid @RequestBody LoginRequest loginRequest) {
        return null;
    }

    @ApiOperation(value = "YouTube 채널 아이디를 입력으로 넣고 twitch id 받음")
    @GetMapping("/youTube")
    public ResponseEntity<?> youTubeId(@CurrentUser UserPrincipal userPrincipal, @RequestParam StreamerMultiPlatform youTubeChannelId){
        StreamerMultiPlatform streamer = streamerMultiPlatformRepository.findByYouTubeChannelId(youTubeChannelId.getYouTubeChannelId()).orElseThrow(() -> new RuntimeException());
        return ResponseEntity.ok(streamer);
    }
    @ApiOperation(value = "twitch id를 입력으로 넣고 YouTube 채널 아이디 받음")
    @GetMapping("/twitch")
    public ResponseEntity<?> twitchId(@CurrentUser UserPrincipal userPrincipal, @RequestParam StreamerMultiPlatform twitchId){
        StreamerMultiPlatform streamer = streamerMultiPlatformRepository.findByTwitchId(twitchId.getTwitchId()).orElseThrow(() -> new RuntimeException());
        return ResponseEntity.ok(streamer);
    }
    @ApiOperation(value = "youtube와 twitch 채널 streamer 등록")
    @PostMapping("/streamer")
    public ResponseEntity<?> insertStreamer(@RequestBody StreamerMultiPlatform information){
        streamerMultiPlatformRepository.save(information);
        return ResponseEntity.ok(information);
    }
}
