package com.rest.api.service.social;

import com.google.gson.Gson;
import com.rest.api.advice.exception.CustomMemberNotFoundException;
import com.rest.api.model.MemberEntity;
import com.rest.api.model.oauth.AuthEntity;
import com.rest.api.model.social.google.RetGoogleAuth;
import com.rest.api.model.social.twitch.RetTwitchAuth;
import com.rest.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GoogleService {
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final Gson gson;
    @Value("${spring.url.base}")
    private String baseUrl;
    @Value("${spring.social.google.client_id}")
    private String googleClientId;
    @Value("${spring.social.google.redirect}")
    private String googleRedirect;
    @Value("${spring.social.google.code_redirect}")
    private String googleCodeRedirect;
    @Value("${spring.social.google.client_secret}")
    private String googleClientSecret;
    @Value("${spring.social.google.code_result_redirect}")
    private String googleCodeResultRedirect;
    @Value("${spring.social.google.google_api_v2_scope}")
    private String scope;
    @Value("${spring.social.google.url.google_api_v2_user}")
    private String googleUserRequestUrl;
    @Value("${spring.social.google.accept}")
    private String accept;
    @Value("${spring.social.google.youtube_data_api_v3}")
    private String youtubeDataAPIuri;

    public String getImplicitCodeFlowUrl() {
        StringBuilder url = new StringBuilder()
                .append(env.getProperty("spring.social.google.url.authorize"))
                .append("?scope=").append(scope)
                .append("&state=state_parameter_passthrough_value")
                .append("&redirect_uri=").append(baseUrl).append(googleCodeRedirect)
                //.append("&access_type=offline")
                .append("&response_type=code")
                .append("&client_id=").append(googleClientId);
        return url.toString();
    }

    public RetGoogleAuth getGoogleTokenInfo(String code) {
        // Set header : Content-type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "oauth2.googleapis.com");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("code", code);
        params.add("redirect_uri", baseUrl + googleCodeRedirect);

        // Set http entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.google.url.token"), request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("getGoogleTokenIfo : " + response.getBody());
            return gson.fromJson(response.getBody(), RetGoogleAuth.class);
        }
        return null;
    }

    public String getYouTubeDataAPI(String accessToken, String youtubeService) {
        StringBuilder url = new StringBuilder()
                .append(youtubeDataAPIuri)
                .append(youtubeService)
                .append("?part=snippet")
                .append("&mine=true")
                .append("&access_token=").append(accessToken);
        return url.toString();
    }

//    public RetGoogleAuth getTwitchAccessTokenWithRefreshToken(String email) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Accept", "application/x-www-form-urlencoded");
//        headers.add("Client-ID", googleClientId);
//        headers.add("Host", "accounts.google.com");
//        MemberEntity member = memberRepository.findByEmail(email).orElseThrow(CustomMemberNotFoundException::new);;
//        List<AuthEntity> authList = member.getAuth();
//        String refreshToken = null;
//        for(AuthEntity auth : authList){
//            if(auth.getAuth_provider().equals("google"))
//                refreshToken = auth.getRefresh_token();
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        // Set parameter
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "refresh_token");
//        params.add("refresh_token", refreshToken);
//        params.add("client_id", googleClientId);
//        params.add("client_secret", googleClientSecret);
//
//        // Set http entity
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.google.url.token").concat("--data-urlencode"), request, String.class);
//        if (response.getStatusCode() == HttpStatus.OK) {
//            System.out.println(response.getBody());
//            return gson.fromJson(response.getBody(), RetGoogleAuth.class);
//        }
//
//        return null;
//    }
}
