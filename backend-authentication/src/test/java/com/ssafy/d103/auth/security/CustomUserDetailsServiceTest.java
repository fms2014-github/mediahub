package com.ssafy.d103.auth.security;

import com.ssafy.d103.auth.exception.ResourceNotFoundException;
import com.ssafy.d103.auth.model.MemberEntity;
import com.ssafy.d103.auth.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomUserDetailsServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void loadMemberById() {
        MemberEntity memberEntity = memberRepository.findById(Long.parseLong("1")).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", 1));
        System.out.println(memberEntity);
    }
}