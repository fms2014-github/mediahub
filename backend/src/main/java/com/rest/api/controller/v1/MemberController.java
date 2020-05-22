package com.rest.api.controller.v1;

import com.rest.api.model.MemberEntity;
import com.rest.api.model.response.ListResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.config.security.JwtTokenProvider;
import com.rest.api.service.MemberService;

import com.rest.api.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"1. Member"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;
	
    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "/index";
    }

//        // 회원가입 페이지
//        @GetMapping("/user/signup")
//        public String dispSignup() {
//            return "/signup";
//        }

//    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다")
//    @PostMapping(value = "/member")
//    public SingleResult<MemberEntity> execSignup(MemberDto memberDto) {
//        return responseService.getSingleResult(memberService.joinUser(memberDto));
//    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping(value = "/members")
    public ListResult<MemberEntity> findAllUser() {
        System.out.println("member 조회 테스트");
        System.out.println("테이블 초기화 테스트");
        return responseService.getListResult(memberService.findAllMember());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @GetMapping(value = "/member")
    public SingleResult<MemberEntity> findByMemberEmail(@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println(email);
        return responseService.getSingleResult(memberService.findByEmail(email));
    }




}