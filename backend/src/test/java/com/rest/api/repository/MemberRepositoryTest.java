package com.rest.api.repository;

import com.rest.api.model.MemberEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void memberRepoTest() {
        String email = "12345";

        memberRepository.save(MemberEntity.builder()
        .email(email)
        .password(passwordEncoder.encode("12345"))
        .roles(Collections.singletonList("ROLE_USER"))
        .build());

        Optional<MemberEntity> member = memberRepository.findByEmail(email);

        assertNotNull(member);
        assertTrue(member.isPresent());
        assertEquals(member.get().getEmail(), email);
        assertThat(member.get().getEmail(), is(email));
    }
}