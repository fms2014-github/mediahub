package com.ssafy.d103.auth.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @GetMapping("/information")
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

    //라벨 위치 수정
    @ApiOperation(value = "라벨 위치 변경 요청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "superLabelId", value = "상위 라벨 id", required = true),
            @ApiImplicitParam(name = "subLabelId", value = "하위 라벨 id", required = true)
    })
    @PutMapping("/label/location")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateLabelLocation(@RequestParam String superLabelId, @RequestParam String subLabelId, @CurrentUser UserPrincipal userPrincipal){
        labelService.updateLabelLocation(Long.parseLong(subLabelId), Long.parseLong(subLabelId));
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "라벨 정보 변경 요청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "labelId", value = "라벨 id", required = true),
            @ApiImplicitParam(name = "labelName", value = "라벨 이름", required = true)
    })
    @PutMapping("/label")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateLabelInformation(@RequestParam String labelId, @RequestParam String labelName,
                                                 @CurrentUser UserPrincipal userPrincipal){
        labelService.updateLabelInformation(Long.parseLong(labelId), labelName);
        return new ResponseEntity(HttpStatus.OK);
    }


    //라벨 정보 수정
    //라벨 삭제
    //라벨 생성
    //채널 추가
    //채널 삭제

}
