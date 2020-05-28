package com.ssafy.d103.auth.twitch;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssafy.d103.auth.model.Auth;
import com.ssafy.d103.auth.model.Member;
import com.ssafy.d103.auth.repository.MemberRepository;
import com.ssafy.d103.auth.twitch.dto.FollowsDto;
import com.ssafy.d103.auth.twitch.model.RetTwitchAuth;
import com.ssafy.d103.auth.twitch.model.TwitchUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class TwitchService {
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final Gson gson;
    @Value("${social.base_url}")
    private String baseUrl;
    @Value("${social.twitch.client_id}")
    private String twitchClientId;
    @Value("${social.twitch.client_secret}")
    private String twitchClientSecret;
    @Value("${social.twitch.redirect}")
    private String twitchRedirect;
    @Value("${social.twitch.code_redirect}")
    private String twitchCodeRedirect;
    @Value("${social.twitch.code_result_redirect}")
    private String twitchCodeResultRedirect;
    @Value("${social.twitch.twitch_api_v5_scope}")
    private String scope;
    @Value("${social.twitch.url.accept}")
    private String accept;
    @Value("${social.twitch.url.twitch_api_v5_user}")
    private String twitchUserRequestUrl;
    @Value("${social.twitch.url.authorize}")
    private String authorize;

    /**
     * 트위치 인증 URI
     * @return 인증 주소 반환
     */
    public String getImplicitCodeFlowUrl() {
        System.out.println(authorize);
        StringBuilder url = new StringBuilder()
                .append(authorize)
                .append("?client_id=").append(twitchClientId)
                .append("&response_type=code")
                .append("&redirect_uri=").append(baseUrl).append(twitchCodeRedirect)
                .append("&scope=").append(scope);
        return url.toString();
    }

    public String getAuthorizationCodeFlowUrl() {
        StringBuilder url = new StringBuilder()
                .append(env.getProperty("social.twitch.url.token"))
                .append("?client_id=").append(twitchClientId)
                .append("&response_type=token")
                .append("&redirect_uri=").append(baseUrl).append(twitchRedirect)
                .append("&scope=").append(scope);
        return url.toString();
    }

    public String getClientCredentialsFlowUrl() {
        StringBuilder url = new StringBuilder()
                .append(env.getProperty("social.twitch.url.token"))
                .append("?client_id=").append(twitchClientId)
                .append("&response_type=token")
                .append("&redirect_uri=").append(baseUrl).append(twitchRedirect)
                .append("&scope=").append(scope);
        return url.toString();
    }

    /**
     *
     * 트위치 인증 URI에서 받아온 code 정보로 access token, refresh token 발급
     *
     * @param code
     * @return
     */
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
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("social.twitch.url.token"), request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());
            return gson.fromJson(response.getBody(), RetTwitchAuth.class);
        }
        return null;
    }

    /**
     *
     * 트위치 팔로우 리스트 받아오기기
     * curl -H 'Accept: application/vnd.twitchtv.v5+json' \
     * -H 'Client-ID: uo6dggojyb8d6soh92zknwmi5ej1q2' \
     * -X GET 'https://api.twitch.tv/kraken/users/{twitch_user_id}/follows/channels'
     *
     * @param twitchUserId twitch 개인 고유 id 필요
     * @return List<FollowsDto> userid에 해당하는 팔로우하는 모든 채널
     */
    public List<FollowsDto> getTwitchAllChannelsByUser(String twitchUserId){
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
        List<FollowsDto> channelList = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
            JsonElement jsonElement = jsonObject.get("follows");
            FollowsDto[] channels = gson.fromJson(jsonElement, FollowsDto[].class);
            return Arrays.asList(channels);
        }
        return null;
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
        Optional<Member> member = memberRepository.findByEmail(email);
        List<Auth> authList = (List) member.get().getAuth();
        String refreshToken = null;
        for(Auth auth : authList){
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
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("social.twitch.url.token").concat("--data-urlencode"), request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            RetTwitchAuth retTwitchAuth = gson.fromJson(response.getBody(), RetTwitchAuth.class);
            System.out.println("getTwitchTokenIfo : " + retTwitchAuth);
            return retTwitchAuth;
        }
        return null;
    }

    /*
    유저 정보 받아오기
     */
    public TwitchUser getTwitchUserInfo(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", env.getProperty("social.twitch.url.accept"));
        headers.add("Client-ID", twitchClientId);
        headers.set("Authorization", "OAuth ".concat(accessToken));

        StringBuilder url = new StringBuilder()
                .append(twitchUserRequestUrl);

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, String.class);
        TwitchUser twitchUser = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            twitchUser = gson.fromJson(response.getBody(), TwitchUser.class);
            return twitchUser;
        }
        return null;
    }

}