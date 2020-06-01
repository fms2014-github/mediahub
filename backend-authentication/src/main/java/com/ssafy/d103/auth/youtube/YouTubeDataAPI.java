package com.ssafy.d103.auth.youtube;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class YouTubeDataAPI {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;

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

    public static YouTube getYouTubeService(String refreshToken){
        try {
            Credential credential = YouTubeDataAPI.createCredentialWithAccessTokenOnlyTest(refreshToken);
            if (!refreshToken.isEmpty()) {
                //Credential credential = YouTubeDataAPI.createCredentialWithAccessTokenOnlyTest(refreshToken);
                System.out.println(credential.getAccessToken());
                System.out.println(credential.getRefreshToken());
                //Credential credential_refresh = YouTubeDataAPI.createCredentialWithRefreshToken(refreshToken);
//            System.out.println(credential_refresh.getRefreshToken());
//            System.out.println(credential_refresh.getAccessToken());
            } else {
                System.out.println("refreshToken.isEmpty");
            }
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