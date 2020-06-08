package com.ssafy.d103.auth.youtube;

import com.google.api.services.youtube.model.ChannelListResponse;
import com.ssafy.d103.auth.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class YoutubeDemoTests {

    @Test
    public void channels() throws IOException{
//        UserPrincipal userPrincipal = Mockito.mock(UserPrincipal.class);
//        ChannelListResponse result = YouTubeDataAPI.getYouTubeService(userPrincipal)
//                .channels()
//                .list("id,snippet,brandingSettings,contentDetails,invideoPromotion,statistics,topicDetails")
//                .setForUsername("NoCopyrightSounds")
//                .execute();
//        System.out.println(result);
    }
}
