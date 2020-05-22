package com.rest.api.service.social;

import com.google.gson.Gson;
import com.rest.api.model.social.twitch.RetTwitchAuth;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TwitchServiceTest {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private Gson gson;

    @Value("${spring.url.base}")
    private String baseUrl;
    @Value("${spring.social.twitch.client_id}")
    private String twitchClientId;
    @Value("${spring.social.twitch.redirect}")
    private String twitchRedirect;
    @Value("${spring.social.twitch.code_redirect}")
    private String twitchCodeRedirect;
    @Value("${spring.social.twitch.client_secret}")
    private String twitchClientSecret;
    @Value("${spring.social.twitch.code_result_redirect}")
    private String twitchCodeResultRedirect;
    @Value("${spring.social.twitch.twitch_api_v5_scope}")
    private String scope;
    @Value("${spring.social.twitch.url.twitch_api_v5_user}")
    private String twitchUserRequestUrl;
    @Value("${spring.social.twitch.accept}")
    private String accept;

//    @Test
    void getTwitchAccessTokenWithRefreshToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", "t5l28r3ceieqnckzqfyrd31n9ki0r1h0de0eso5yyaquoy3bgh");
        params.add("client_id", twitchClientId);
        params.add("client_secret", twitchClientSecret);


        // Set http entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.twitch.url.token"), request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("리프레시 토큰으로 토큰 재발급받기 ********************");
            System.out.println(response.getBody());
            RetTwitchAuth rta = gson.fromJson(response.getBody(), RetTwitchAuth.class);
            System.out.println(rta);
            System.out.println("****end****** 리프레시 토큰으로 토큰 재발급받기 종료*********end***********");
        }


    }

    @Test
    void getTwitchAllChannelsByUser() {
        String twitchUserId = "131655528";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.twitchtv.v5+json");
        headers.add("Client-ID", twitchClientId);
        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();


        StringBuilder url = new StringBuilder()
                .append("https://api.twitch.tv/kraken/users/131655528/follows/channels");


        // Set http entity
        HttpEntity entity = new HttpEntity(headers);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, String.class);
//                restTemplate.getForEntity(url.toString(), String.class, request);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Twitch Follow List ***************************");
            System.out.println(response.getBody());
            System.out.println("Twitch Follow List **********endend***********");
//            return gson.fromJson(response.getBody(), ListResult<ChannelEntity.class);
        }
    }

}