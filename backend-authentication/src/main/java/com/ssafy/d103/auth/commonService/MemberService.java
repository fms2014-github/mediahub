package com.ssafy.d103.auth.commonService;

import com.ssafy.d103.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 왠지 몰라도 CustomUserDetailService가 있어서 이거 안쓸듯..
 * 보고 삭제
 */
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
}
