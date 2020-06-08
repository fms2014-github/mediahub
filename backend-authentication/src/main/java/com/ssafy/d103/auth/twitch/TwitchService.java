package com.ssafy.d103.auth.twitch;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssafy.d103.auth.commonService.AuthService;
import com.ssafy.d103.auth.exception.AuthException;
import com.ssafy.d103.auth.exception.InvalidAccessTokenException;
import com.ssafy.d103.auth.exception.MemberNotFoundException;
import com.ssafy.d103.auth.exception.MemberRefreshTokenNotFoundException;
import com.ssafy.d103.auth.model.Auth;
import com.ssafy.d103.auth.model.Member;
import com.ssafy.d103.auth.repository.AuthRepository;
import com.ssafy.d103.auth.repository.ChannelRepository;
import com.ssafy.d103.auth.repository.MemberRepository;
import com.ssafy.d103.auth.twitch.dto.TwitchChannelDto;
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
    private final AuthRepository authRepository;
    private final Environment env;
    private final Gson gson;
    @Value("${social.base_url}")
    private String baseUrl;
    @Value("${social.front_base_url}")
    private String frontBaseUrl;
    @Value("${social.twitch.client_id}")
    private String twitchClientId;
    @Value("${social.twitch.client_secret}")
    private String twitchClientSecret;
    @Value("${social.twitch.redirect}")
    private String twitchRedirect;
    @Value("${social.twitch.code_redirect}")
    private String twitchCodeRedirect;
    @Value("${social.twitch.token_redirect}")
    private String twitchTokenRedirect;
    @Value("${social.twitch.twitch_api_v5_scope}")
    private String scope;
    @Value("${social.twitch.url.accept}")
    private String accept;
    @Value("${social.twitch.url.twitch_api_v5_user}")
    private String twitchUserRequestUrl;
    @Value("${social.twitch.url.twitch_api_v5_users}")
    private String twitchUserFollowRequestUrl;
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
                .append("&scope=").append(scope)
                .append("&redirect_uri=").append(frontBaseUrl).append(twitchCodeRedirect);
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
        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", twitchClientId);
        params.add("client_secret", twitchClientSecret);
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", baseUrl+twitchTokenRedirect);

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
                .append(twitchUserFollowRequestUrl)
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
     * @param id user pk
     * @return Twitch Access Token 재발급 요청한 결과 RetTwitchAuth
     *
     * 1. email 인자로 받음
     * 2. Database에서 Twitch Refresh token 가져옴
     * 3. POST 요청 만들어서 json Data 요청
     * 4. 실패시 Return null
     * 5. 성공시 Return RetTwitchAuth
     */
    public Auth getTwitchAccessTokenWithRefreshToken(long id) {
        Member member = memberRepository.findById(id).orElseThrow(()-> new MemberNotFoundException(id));
        List<Auth> authList = (List) member.getAuth();
        long authId = 0;
        String refreshToken = null;
        for(Auth auth : authList){
            if(auth.getAuth_provider().equals("twitch")) {
                authId = auth.getId();
                refreshToken = auth.getRefresh_token();
            }
        }

        if(refreshToken == null){
            throw new MemberRefreshTokenNotFoundException(member.getId());
        }
        System.out.println("********************** 리프레시 토큰 :: "+refreshToken);
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
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("social.twitch.url.token"), request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            RetTwitchAuth retTwitchAuth = gson.fromJson(response.getBody(), RetTwitchAuth.class);
            Auth auth = authRepository.findById(authId).get();
            auth.setAccess_token(retTwitchAuth.getAccess_token());
            auth.setRefresh_token(retTwitchAuth.getRefresh_token());
            return auth;
        }
        return null;
    }

    /**
     * 유저 정보 accessToken으로 요청
     * @param accessToken
     * @return
     */
    public TwitchUser getTwitchUserInfo(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", accept);
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

    /**
     * 트위치 채널 팔로우
     * @param channelId
     * @param userId
     * @param accessToken
     * @return
     */
    public TwitchChannelDto followTwitchChannel(String channelId, String userId, String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", accept);
        headers.add("Client-ID", twitchClientId);
        headers.set("Authorization", "OAuth ".concat(accessToken));

        StringBuilder url = new StringBuilder()
                .append(twitchUserFollowRequestUrl)
                .append("/")
                .append(userId)
                .append("/follows/channels/")
                .append(channelId);

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.PUT, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
            JsonElement jsonElement = jsonObject.get("channel");
            return gson.fromJson(jsonElement, TwitchChannelDto.class);
        }

        return null;
    }

    /**
     * 트위치 채널 언팔로우
     * @param channelId
     * @param accessToken
     * @param userId
     * @return
     */
    public boolean unFollowTwitchChannel(String channelId, String accessToken, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", accept);
                headers.add("Client-ID", twitchClientId);
        headers.set("Authorization", "OAuth ".concat(accessToken));

        StringBuilder url = new StringBuilder()
                .append(twitchUserFollowRequestUrl)
                .append("/")
                .append(userId)
                .append("/follows/channels/")
                .append(Long.parseLong(channelId));

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.DELETE, entity, String.class);
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return true;
        }else if(response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new InvalidAccessTokenException();
        }

        return false;
    }
}