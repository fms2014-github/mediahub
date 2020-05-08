package com.rest.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.rest.api.advice.exception.CustomMemberExistException;
import com.rest.api.advice.exception.CustomMemberNotFoundException;
import com.rest.api.dto.MemberDto;
import com.rest.api.model.MemberEntity;
import com.rest.api.model.Role;
import com.rest.api.model.response.CommonResult;
import com.rest.api.repository.MemberRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResponseService responseService;

    @Transactional
    public CommonResult joinUser(String email, String password) {
        memberRepository.save(MemberEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return responseService.getSuccessResult();
    }

    @Transactional
    public void joinSocialJoin(String email, String provider){

        Optional<MemberEntity> member = memberRepository.findByEmailAndProvider(email, provider);

        if(member.isPresent())
            throw  new CustomMemberExistException();

        memberRepository.save(MemberEntity.builder()
                .email(email)
                .provider(provider)
                .roles(Collections.singletonList("ROLE_USER"))
                .build()
        );
    }

    @Transactional
    public List<MemberEntity> findAllMember() {
        return memberRepository.findAll();
    }

    @Transactional
    public MemberEntity findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(CustomMemberNotFoundException::new);
    }

//    @Override
//    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
//        MemberEntity userEntity = memberRepository.findByEmail(userEmail).orElseThrow(CustomMemberNotFoundException::new);
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        System.out.println("this mail is logging :: "+userEmail);
//        if (("admin@example.com").equals(userEmail)) {
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
//        } else {
//            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
//        }
//
//        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
//    }
    public UserDetails loadUserByUsername(String userPk) {
        return memberRepository.findById(Long.valueOf(userPk)).orElseThrow(CustomMemberNotFoundException::new);
    }

    public MemberEntity findByEmailAndProvider(String email, String provider){
        return memberRepository.findByEmailAndProvider(email, provider).orElseThrow(CustomMemberNotFoundException::new);
    }
}