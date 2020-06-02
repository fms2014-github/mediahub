package com.ssafy.d103.auth.security.oauth2;

import com.ssafy.d103.auth.exception.OAuth2AuthenticationProcessingException;
import com.ssafy.d103.auth.model.AuthProvider;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.model.Member;
import com.ssafy.d103.auth.model.RoleType;
import com.ssafy.d103.auth.repository.LabelRepository;
import com.ssafy.d103.auth.repository.MemberRepository;
import com.ssafy.d103.auth.security.UserPrincipal;
import com.ssafy.d103.auth.security.oauth2.user.OAuth2UserInfo;
import com.ssafy.d103.auth.security.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        System.out.println("access token : "+oAuth2UserRequest.getAccessToken().getTokenValue());
        System.out.println("access token : ");
        System.out.println("token type" + oAuth2UserRequest.getAccessToken().getTokenType().getValue());
        System.out.println("scope" + oAuth2UserRequest.getAccessToken().getScopes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Member> userOptional = memberRepository.findByEmail(oAuth2UserInfo.getEmail());
        Member member;
        if(userOptional.isPresent()) {
            member = userOptional.get();
            if(!member.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        member.getProvider() + " account. Please use your " + member.getProvider() +
                        " account to login.");
            }
            member = updateExistingUser(member, oAuth2UserInfo);
        } else {
            System.out.println("*********************멤버 저장, 루트 라벨 설정*********************");
            member = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
            Label label = createRootLabel(member);
            System.out.println(member.getId()+", "+member.getEmail());
            System.out.println(label.getId()+", "+ label.getMemberId()+", "+label.getLabelName());
            member = setMemberRootLabel(member, label.getId());
            System.out.println(member.getId()+", "+member.getEmail()+", "+member.getRootLabelId());

            System.out.println("===========멤버 저장, 루트 라벨 설정========");

        }

        return UserPrincipal.create(member, oAuth2User.getAttributes());
    }

    private Member registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Member member = new Member();

        member.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        member.setProviderId(oAuth2UserInfo.getId());
        member.setName(oAuth2UserInfo.getName());
        member.setEmail(oAuth2UserInfo.getEmail());
        member.setProfileUrl(oAuth2UserInfo.getImageUrl());
        member.setFirstLogin(0);
        member.setRole(RoleType.MEMBER);
//        memberEntity.setLabelList(createRootLabel());
        return memberRepository.save(member);
    }

    private Label createRootLabel(Member member){
        Label label = new Label("루트 라벨");
        label.setMemberId(member.getId());
        return labelRepository.save(label);
    }

    private Member setMemberRootLabel(Member member, Long rootLabelId){
        member.setRootLabelId(rootLabelId);
        return memberRepository.save(member);
    }

    private Member updateExistingUser(Member existingMember, OAuth2UserInfo oAuth2UserInfo) {
        existingMember.setName(oAuth2UserInfo.getName());
        existingMember.setProfileUrl(oAuth2UserInfo.getImageUrl());
        return memberRepository.save(existingMember);
    }

    private List<Label> createRootLabel(){
        List<Label> list = new LinkedList<>();
        Label label = new Label();
        label.setLabelName("My Category");
        list.add(label);
        return list;
    }
}
