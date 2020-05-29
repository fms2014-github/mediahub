package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.dto.AuthDto;
import com.ssafy.d103.auth.dto.MemberDto;
import com.ssafy.d103.auth.exception.ResourceNotFoundException;
import com.ssafy.d103.auth.model.Auth;
import com.ssafy.d103.auth.model.Member;
import com.ssafy.d103.auth.repository.MemberRepository;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import com.ssafy.d103.auth.security.UserPrincipal;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collector;
import java.util.stream.Collectors;

@Api(tags = {"user"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member")
public class MemberController {

    private final CustomUserDetailsService memberService;
    private final LabelService labelService;
    /**
     * 기본 멤버 정보, 라벨
     *
     * @param userPrincipal
     * @return
     */
//    @Api()
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MemberDto> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        Member member = memberService.loadMemberById(userPrincipal.getId());
        MemberDto memberDto = new MemberDto().builder()
                .name(member.getName())
                .email(member.getEmail())
                .provider(member.getProvider())
                .profileUrl(member.getProfileUrl())
                .providerId(member.getProviderId())
                .firstLogin(member.getFirstLogin())
                .roles(member.getRole())
                .auth(
                        member.getAuth().stream()
                        .map(auth -> {
                            auth = (Auth) auth;
                            return new AuthDto().builder()
                                    .access_token(auth.getAccess_token())
                                    .provider(auth.getAuth_provider())
                                    .build();
                        }).collect(Collectors.toList())
                )
                .label(labelService.getLabelById(member.getRootLabelId()))
                .build();
        return new ResponseEntity(memberDto, HttpStatus.OK);
    }

    /**
     *
     * @param userPrincipal
     * @return
     */
//    @GetMapping("/user/auth")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<?> getUserAuth(@CurrentUser UserPrincipal userPrincipal){
//        Optional<MemberEntity> member = memberRepository
//       .findById(userPrincipal.getId());
//        List<AuthEntity> authList = member.get().getAuth();
//
//        if(authList == null){
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        }
//
//        List<AuthDto> authDtoList = new LinkedList<>();
//        for(AuthEntity authEntity : authList){
//            authDtoList.add(new AuthDto().builder()
//                                .access_token(authEntity.getAccess_token())
//                                .provider(authEntity.getAuth_provider())
//                                .build());
//        }
//
//        return new ResponseEntity(authDtoList, HttpStatus.OK);
//    }

}
