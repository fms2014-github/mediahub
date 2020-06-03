package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.payload.AuthResponse;
import com.ssafy.d103.auth.payload.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PlatFormController {
    @PostMapping("/login")
    public ResponseEntity<?> platform(@Valid @RequestBody LoginRequest loginRequest) {

        return null;
    }
}
