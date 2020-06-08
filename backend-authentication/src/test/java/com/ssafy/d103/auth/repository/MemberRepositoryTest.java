package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void memberRepotest(){

//        memberRepository.deleteAll();
//
//        Member member = new Member("김상헌", "atlanboa", AuthProvider.google, 0);
//
//
//
//        memberRepository.saveAndFlush(member);
//
//        Member rMember = memberRepository.findByEmail("atlanboa").get();
//
//        Auth auth = new Auth();
//        auth.setAuth_provider(AuthProvider.twitch.toString());
//        auth.setAccess_token("access_token");
//        auth.setRefresh_token("refresh_token");
//        auth.setToken_type("bearer");
//        auth.setUserId(1234);
//        rMember.getAuth().add(auth);
//        memberRepository.saveAndFlush(rMember);
//
//        rMember = memberRepository.findByEmail("atlanboa").get();
//
//        System.out.println("여기까지 테스트");
//        Label label = new Label("루트 라벨");
//        label.setMemberId(rMember.getId());
//
//        labelRepository.saveAndFlush(label);
//        Label rLabel = labelRepository.findByMemberId(rMember.getId()).get();
//
//
//
//        Channel channel = new Channel();
//        channel.setProvider("트위치");
//        channel.setChannelId("트위치 채널 아이디");
//        channel.setProfileImg("트위치 채널 프로필 이미지");
//        channel.setFollower(1000L);
//        channel.setSubscriber(800L);
//        channel.setDescription("채널 설명입니다");
//
//        rLabel.getChannels().add(channel);
//        labelRepository.saveAndFlush(rLabel);
//
//
//        Long labelId = rLabel.getId();
//        rLabel = labelRepository.findById(labelId).get();
//
//        System.out.println("this is test");

        Member member = memberRepository.findById(3L).get();
        System.out.println("Test 시작입니다 *******************");
        for(Auth a : member.getAuth()){
            System.out.println(a.getAuth_provider()+",   "+ a.getMember().getName());
        }
        System.out.println("Test 끝입니다 *******************");
    }
}
