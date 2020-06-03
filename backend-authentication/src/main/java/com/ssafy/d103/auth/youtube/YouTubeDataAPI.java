package com.ssafy.d103.auth.youtube;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Configuration
public class YouTubeDataAPI {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;
    private static AppIdentityService appIdentityService = AppIdentityServiceFactory.getAppIdentityService();

    @Value("${social.google.api_key}")
    private static String googleApiKey;

    @Value("${social.google.client_id}")
    private static String googleClientId;

    @Value("${social.google.client_secret}")
    private static String googleClientSecret;

    @Value("${social.google.client_id}")
    private void setGoogleClientId(String clientId){
        googleClientId = clientId;
    }

    @Value("${social.google.client_secret}")
    private void setGoogleClientSecret(String clientSecret){
        googleClientSecret = clientSecret;
    }

    @Value("${social.google.api_key}")
    private void setGoogleApiKey(String apiKey){
        googleApiKey = apiKey;
    }
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    public static Credential createCredentialWithAccessTokenOnlyTest(String refreshToken) throws IOException {
        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(HTTP_TRANSPORT,
                                                                    JSON_FACTORY,
                                                                    refreshToken,
                                                                    googleClientId,
                                                                    googleClientSecret).execute();
        return new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(tokenResponse);
    }

    public static Credential createCredentialWithAccessTokenOnly(TokenResponse tokenResponse){
        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod());
        return new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(tokenResponse);
    }

    public static Credential createCredentialWithRefreshToken(String refreshToken) throws IOException {
        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(HTTP_TRANSPORT,
                JSON_FACTORY,
                refreshToken,
                googleClientId,
                googleClientSecret).execute();
        return new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).setTransport(
                HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setTokenServerUrl(
                        new GenericUrl("https://oauth2.googleapis.com/token"))
                .setClientAuthentication(new BasicAuthentication("", ""))
                .build()
                .setFromTokenResponse(tokenResponse);
    }
    public static Credential getCredential() throws IOException {
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("client_secret.json"))
                .createScoped(scopes);
        return credential;
    }

    public static AccessToken getCredential2() throws IOException {
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("client_secret.json"));
        credentials.refreshIfExpired();
        //AccessToken token = credentials.getAccessToken();
        AccessToken token = credentials.refreshAccessToken();
        return token;
    }

    public static YouTube getYouTubeService(String refreshToken){
        try {
            Credential credential = YouTubeDataAPI.createCredentialWithAccessTokenOnlyTest(getCredential2().getTokenValue());
            return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("")
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


//        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
//            @Override
//            public void initialize(HttpRequest request) throws IOException {
//                System.out.println(request);
//            }
//        }).setApplicationName("Media-platform-management")
//                .setYouTubeRequestInitializer(new YouTubeRequestInitializer(googleApiKey))
//                .build();
    }
}