package com.ssafy.d103.auth.commonService;

import com.ssafy.d103.auth.exception.AuthException;
import com.ssafy.d103.auth.model.Auth;
import com.ssafy.d103.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private AuthRepository authRepository;
    public void deleteAuth(Long authId){
        Auth targetAuth = authRepository.findById(authId).orElseThrow(() -> new AuthException(authId));
        authRepository.delete(targetAuth);
    }
}
