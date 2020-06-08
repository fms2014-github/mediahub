package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.AuthApplication;
import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.commonService.MemberService;
import com.ssafy.d103.auth.dto.AuthDto;
import com.ssafy.d103.auth.dto.MemberDto;
import com.ssafy.d103.auth.exception.LabelNotFoundException;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.model.Member;
import com.ssafy.d103.auth.repository.LabelRepository;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AuthApplication.class)
@WebAppConfiguration
class MemberControllerTest {

    @Autowired
    private CustomUserDetailsService memberService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private EntityManager em;

//    @Test
//    @Transactional
//    void getCurrentUser(){
//        Member member = memberService.loadMemberById(1L);
//        MemberDto memberDto = new MemberDto().builder()
//                .name(member.getName())
//                .email(member.getEmail())
//                .provider(member.getProvider())
//                .profileUrl(member.getProfileUrl())
//                .providerId(member.getProviderId())
//                .firstLogin(member.getFirstLogin())
//                .roles(member.getRole())
//                .auth(
//                        member.getAuth().stream()
//                                .map(auth ->
//                                        new AuthDto().builder()
//                                                .access_token(auth.getAccess_token())
//                                                .provider(auth.getAuth_provider())
//                                                .build()
//                                ).collect(Collectors.toList())
//                )
//                .label(labelService.getLabelById(member.getRootLabelId()))
//                .build();
//
//        System.out.println("**************************getCurrentUser Test***************************");
//        System.out.println("멤버 이름 : "+memberDto.getName());
//        for(AuthDto a : memberDto.getAuth()){
//            System.out.println("멤버 access_token : "+a.getAccess_token());
//            System.out.println("멤버 인증 제공자 : "+a.getProvider());
//        }
//        System.out.println("**************************getCurrentUser Test End***************************");
//    }

    @Test
    @Transactional
    void updateLabelLocation() {
        System.out.println("**************************updateLabelLocation Test***************************");
//        Label rootLabel = labelService.getLabelById(1L);
        Label superLabel = labelRepository.findById(1L).orElseThrow(() -> new LabelNotFoundException(3L));
        Label subLabel = labelRepository.findById(3L).orElseThrow(() -> new LabelNotFoundException(1L));

        subLabel.setSuperLabel(superLabel);
        labelRepository.saveAndFlush(subLabel);
//        labelService.updateLabelLocation(1L, 3L);
        Label label = labelRepository.findById(1L).get();
//        Label label = labelService.getLabelById(1L);
        System.out.println("**************************updateLabelLocation Test***************************");
    }
//    @Test
//    void updateLabelInformation() {
//        System.out.println();
//        Label label = labelService.getLabelById(3L);
//        labelService.updateLabelInformation(3L, "수정된 라벨 이름");
//
//        Label rootLabel = labelService.getLabelById(1L);
//    }
//
//    @Test
//    void deleteLabel() {
//    }
}