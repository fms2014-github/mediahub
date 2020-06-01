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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class YouTubeService {
    @Autowired
    private RestTemplate restTemplate;
    private final Gson gson;
    private final Environment env;
    @Value("${social.base_url}")
    private String baseUrl;
    @Value("${social.front_base_url}")
    private String frontBaseUrl;
    @Value("${social.google.client_id}")
    private String googleClientId;
    @Value("${social.google.code_redirect}")
    private String googleCodeRedirect;
    @Value("${social.google.client_secret}")
    private String googleClientSecret;
    @Value("${social.google.token_redirect}")
    private String googleTokenRedirect;
    @Value("${social.google.google_api_v2_scope}")
    private String scope;
    @Value("${social.google.accept}")
    private String accept;
    @Value("${social.google.youtube_data_api_v3}")
    private String youtubeDataAPIuri;

    public String getImplicitCodeFlowUrl() {
        StringBuilder url = new StringBuilder()
                .append(env.getProperty("social.google.url.authorize"))
                .append("?scope=").append(scope)
                .append("&state=state_parameter_passthrough_value")
                .append("&access_type=offline")
                .append("&response_type=code")
                .append("&approval_prompt=force")
                .append("&client_id=").append(googleClientId)
                .append("&redirect_uri=").append(frontBaseUrl).append(googleCodeRedirect);
        //ResponseEntity<String> response = restTemplate.getForEntity(url.toString(),String.class);
        //System.out.println(response);
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
        params.add("access_type", "offline");
        params.add("prompt","consent");
//        params.add("approval_prompt","force");
        params.add("redirect_uri", "http://k02d1031.p.ssafy.io" + googleTokenRedirect);
        System.out.println("params : " + params);
        // Set http entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("social.google.url.token"), request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("getGoogleTokenIfo : " + response.getBody());
            return gson.fromJson(response.getBody(), RetGoogleAuth.class);
        }
        return null;
    }
    public String getYouTubeSubscriptions(String accessToken, String youtubeService) {
        StringBuilder url = new StringBuilder()
                .append(youtubeDataAPIuri)
                .append(youtubeService)
                .append("?part=snippet")
                .append("&mine=true")
                .append("&access_token=").append(accessToken);
        return url.toString();
    }

    public RetGoogleAuth getRefreshingAccessToken(String refreshToken){
        System.out.println(refreshToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "accounts.google.com");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        StringBuilder sb = new StringBuilder();
        sb.append(refreshToken);
        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        System.out.println(googleClientId);
        System.out.println(googleClientSecret);
        System.out.println(env.getProperty("social.google.url.token"));
        params.add("grant_type", "refresh_token");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("refresh_token", refreshToken.substring(1,refreshToken.length()-1));
        params.add("access_type","offline");

        // Set http entity
        System.out.println("refreshing!!!");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://www.googleapis.com/oauth2/v4/token", request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("getGoogleTokenIfo : " + response.getBody());
            return gson.fromJson(response.getBody(), RetGoogleAuth.class);
        }
        return null;
    }

    ///// google youtube api test
    public String getYouTubeVideoId(String channelId, String accessToken, String youtubeService) {
        StringBuilder url = new StringBuilder()
                .append(youtubeDataAPIuri)
                .append(youtubeService)
                .append("?part=snippet")
                .append("&channelId=").append(channelId)
                .append("eventType=live")
                .append("type=video")
                .append("&access_token=").append(accessToken);
        return url.toString();
    }
}
