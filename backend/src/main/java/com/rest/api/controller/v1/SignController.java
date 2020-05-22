package com.rest.api.controller.v1;

import com.rest.api.advice.exception.CustomEmailSigninFailedException;
import com.rest.api.model.MemberEntity;
import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.config.security.JwtTokenProvider;
import com.rest.api.service.MemberService;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
        MemberEntity member = memberService.findByEmail(email);
        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new CustomEmailSigninFailedException();
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles()));
    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signup(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
                               @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {

        return memberService.joinUser(email, password);
    }

//    @ApiOperation(value = "소셜 로그인", notes = "소셜 회원 로그인을 한다.")
//    @PostMapping(value = "/signin/{provider}")
//    public SingleResult<String> signinByProvider(
//            @ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
//            @ApiParam(value = "소셜 access_token", required = true) @RequestParam String accessToken) {
//        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
//        System.out.println(profile.getId()+" "+ provider);
//        MemberEntity member = memberService.findByEmailAndProvider(String.valueOf(profile.getId()), provider);
//        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles()));
//    }
//
//    @ApiOperation(value = "소셜 계정 가입", notes = "소셜 계정 회원가입을 한다.")
//    @PostMapping(value = "/signup/{provider}")
//    public CommonResult signupProvider(@ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
//                                       @ApiParam(value = "소셜 access_token", required = true) @RequestParam String accessToken,
//                                       @ApiParam(value = "이메일", required = true) @RequestParam String email) {
//        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
//        memberService.joinSocialJoin(String.valueOf(profile.getId()), provider);
//
//        return responseService.getSuccessResult();
//    }
}
