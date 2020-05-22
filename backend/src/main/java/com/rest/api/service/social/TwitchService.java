package com.rest.api.service.social;

import com.google.gson.Gson;
import com.rest.api.model.social.kakao.RetKakaoAuth;
import com.rest.api.model.social.twitch.RetTwitchAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class TwitchService {
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


//    public KakaoProfile getKakaoProfile(String accessToken) {
//        // Set header : Content-type: application/x-www-form-urlencoded
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("Authorization", "Bearer " + accessToken);
//        // Set http entity
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
//        try {
//            // Request profile
//            ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.kakao.url.profile"), request, String.class);
//            if (response.getStatusCode() == HttpStatus.OK)
//                return gson.fromJson(response.getBody(), KakaoProfile.class);
//        } catch (Exception e) {
//            throw new CustomCommunicationException();
//        }
//        throw new CustomCommunicationException();
//    }
    public String getImplicitCodeFlowUrl() {
        StringBuilder url = new StringBuilder()
                .append(env.getProperty("spring.social.twitch.url.token"))
                .append("?client_id=").append(twitchClientId)
                .append("&response_type=token")
                .append("&redirect_uri=").append(baseUrl).append(twitchRedirect)
                .append("&scope=user:edit user:read:email user:edit:follows");
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
}
