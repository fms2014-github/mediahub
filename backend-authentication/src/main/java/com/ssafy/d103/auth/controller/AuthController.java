package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.exception.BadRequestException;
import com.ssafy.d103.auth.model.AuthProvider;
import com.ssafy.d103.auth.model.MemberEntity;
import com.ssafy.d103.auth.payload.ApiResponse;
import com.ssafy.d103.auth.payload.AuthResponse;
import com.ssafy.d103.auth.payload.LoginRequest;
import com.ssafy.d103.auth.payload.SignUpRequest;
import com.ssafy.d103.auth.repository.MemberRepository;
import com.ssafy.d103.auth.security.TokenProvider;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Api(tags = {"auth"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
<<<<<<< HEAD
    private MemberRepository memberRepository;
=======
    private MemberRepository userRepository;
>>>>>>> feature/backend-twitch

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getEmail()+" : "+loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName(signUpRequest.getName());
        memberEntity.setEmail(signUpRequest.getEmail());
        memberEntity.setPassword(signUpRequest.getPassword());
        memberEntity.setProvider(AuthProvider.local);
        //user.setRole(RoleType.USER.toString());

        memberEntity.setPassword(passwordEncoder.encode(memberEntity.getPassword()));

        MemberEntity result = memberRepository.save(memberEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }

}
