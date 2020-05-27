package com.ssafy.d103.auth.youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.ssafy.d103.auth.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Configuration
public class YouTubeDataAPI {
    private static final String APPLICATION_NAME = "YOUTUBE TEST";
    private static File DATA_STORE_DIR = null;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(YouTubeScopes.YOUTUBE_FORCE_SSL);
    private static HttpTransport HTTP_TRANSPORT;
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    @Value("${server.port}")
    private static int port;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static Credential authorize(UserPrincipal userPrincipal) throws IOException {
        System.out.println(userPrincipal.getId());
        DATA_STORE_DIR = new File(System.getProperty("user.home"), ".oauth-credentials");
        InputStream in = YouTubeDataAPI.class.getResourceAsStream("/client_secret.json");
        DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        System.out.println(DATA_STORE_FACTORY);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        LocalServerReceiver localReceiver = new LocalServerReceiver.Builder()
                .setHost("localhost")
                .setPort(8081).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
        return credential;
    }

    public static YouTube getYouTubeService(UserPrincipal userPrincipal) throws IOException {
        Credential credential = authorize(userPrincipal);
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}