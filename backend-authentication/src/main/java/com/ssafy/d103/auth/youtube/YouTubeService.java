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
    @Value("${social.google.client_id}")
    private String googleClientId;
    @Value("${social.google.redirect}")
    private String googleRedirect;
    @Value("${social.google.code_redirect}")
    private String googleCodeRedirect;
    @Value("${social.google.client_secret}")
    private String googleClientSecret;
    @Value("${social.google.code_result_redirect}")
    private String googleCodeResultRedirect;
    @Value("${social.google.google_api_v2_scope}")
    private String scope;
    @Value("${social.google.url.google_api_v2_user}")
    private String googleUserRequestUrl;
    @Value("${social.google.accept}")
    private String accept;
    @Value("${social.google.youtube_data_api_v3}")
    private String youtubeDataAPIuri;

    public String getImplicitCodeFlowUrl() {
        StringBuilder url = new StringBuilder()
                .append(env.getProperty("social.google.url.authorize"))
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
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("social.google.url.token"), request, String.class);
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
