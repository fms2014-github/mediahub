package com.ssafy.d103.auth.youtube;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class YouTubeService {
    @Autowired
    private RestTemplate restTemplate;
    private final Gson gson;
    private final Environment env;
    private String baseUrl = "http://localhost:8080";
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
                .append("&access_type=offline")
                .append("&response_type=code")
                .append("&approval_prompt=force")
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
        params.add("access_type", "offline");
        params.add("approval_prompt","force");

        // Set http entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.google.url.token"), request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("getGoogleTokenIfo : " + response.getBody());
            return gson.fromJson(response.getBody(), RetGoogleAuth.class);
        }
        return null;
    }

}
