package com.rest.api.service.social;

import com.google.gson.Gson;
import com.rest.api.advice.exception.CustomMemberNotFoundException;
import com.rest.api.model.ChannelEntity;
import com.rest.api.model.MemberEntity;
import com.rest.api.model.oauth.AuthEntity;
import com.rest.api.model.response.ListResult;
import com.rest.api.model.social.twitch.RetTwitchAuth;
import com.rest.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TwitchService {
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final Gson gson;
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
//https://id.twitch.tv/oauth2/authorize
// ?client_id=oqnfm929440pohis4h4xd1rfr4cd2u
// &redirect_uri=http://localhost:8080/social/login/twitch/code
// &response_type=code
// &scope=channel_check_subscription channel_commercial channel_editor channel_feed_edit channel_feed_read channel_read channel_stream channel_subscriptions collections_edit communities_edit communities_moderate openid user_blocks_edit user_blocks_read user_follows_edit user_read user_subscriptions viewing_activity_read analytics:read:extensions analytics:read:games bits:read channel:edit:commercial channel:read:subscriptions clips:edit user:edit user:edit:broadcast user:edit:follows user:read:broadcast user:read:email
    public String getImplicitCodeFlowUrl() {
        StringBuilder url = new StringBuilder()
                .append(env.getProperty("spring.social.twitch.url.authorize"))
                .append("?client_id=").append(twitchClientId)
                .append("&response_type=code")
                .append("&redirect_uri=").append(baseUrl).append(twitchCodeRedirect)
                .append("&scope=").append(scope);
        return url.toString();
    }

    public RetTwitchAuth getTwitchTokenInfo(String code) {
        // Set header : Content-type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", twitchClientId);
        params.add("client_secret", twitchClientSecret);
        params.add("code", code);
        params.add("redirect_uri", baseUrl + twitchCodeRedirect);

        // Set http entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.twitch.url.token"), request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());
            return gson.fromJson(response.getBody(), RetTwitchAuth.class);
        }
        return null;
    }

    public String getAuthorizationCodeFlowUrl() {
        StringBuilder url = new StringBuilder()
                .append(env.getProperty("spring.social.twitch.url.token"))
                .append("?client_id=").append(twitchClientId)
                .append("&response_type=token")
                .append("&redirect_uri=").append(baseUrl).append(twitchRedirect)
                .append("&scope=user:edit user:read:email user:edit:follows");
        return url.toString();
    }

    public String getClientCredentialsFlowUrl() {
        StringBuilder url = new StringBuilder()
                .append(env.getProperty("spring.social.twitch.url.token"))
                .append("?client_id=").append(twitchClientId)
                .append("&response_type=token")
                .append("&redirect_uri=").append(baseUrl).append(twitchRedirect)
                .append("&scope=user:edit user:read:email user:edit:follows");
        return url.toString();
    }





    /**
     *
     * 트위치 팔로우 리스트 받아오기기
     * curl -H 'Accept: application/vnd.twitchtv.v5+json' \
     * -H 'Client-ID: uo6dggojyb8d6soh92zknwmi5ej1q2' \
     * -X GET 'https://api.twitch.tv/kraken/users/{twitch_user_id}/follows/channels'
     *
     * @param twitchUserId twitch 개인 고유 id 필요
     * @return String 채널 json data 반환
     */
    public String getTwitchAllChannelsByUser(String twitchUserId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.twitchtv.v5+json");
        headers.add("Client-ID", twitchClientId);

        StringBuilder url = new StringBuilder()
                .append(twitchUserRequestUrl)
                .append("/")
                .append(twitchUserId)
                .append("/follows/channels");

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, String.class);
        String result = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            result = response.getBody();
        }
        return result;
    }


    /**
     *
     * 리프레시 토큰으로 토큰 재발급 받기기
     * POST https://id.twitch.tv/oauth2/token
     * --data-urlencode
     * ?grant_type=refresh_token
     * &refresh_token=<your refresh token>
     * &client_id=<your client ID>
     * &client_secret=<your client secret>
     *
     * @param email user email
     * @return Twitch Access Token 재발급 요청한 결과 RetTwitchAuth
     *
     * 1. email 인자로 받음
     * 2. Database에서 Twitch Refresh token 가져옴
     * 3. POST 요청 만들어서 json Data 요청
     * 4. 실패시 Return null
     * 5. 성공시 Return RetTwitchAuth
     */
    public RetTwitchAuth getTwitchAccessTokenWithRefreshToken(String email) {
        MemberEntity member = memberRepository.findByEmail(email).orElseThrow(CustomMemberNotFoundException::new);;
        List<AuthEntity> authList = member.getAuth();
        String refreshToken = null;
        for(AuthEntity auth : authList){
            if(auth.getAuth_provider().equals("twitch"))
                refreshToken = auth.getRefresh_token();

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);
        params.add("client_id", twitchClientId);
        params.add("client_secret", twitchClientSecret);

        // Set http entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.twitch.url.token").concat("--data-urlencode"), request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());
            return gson.fromJson(response.getBody(), RetTwitchAuth.class);
        }

        return null;
    }
}
