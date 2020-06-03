package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.dto.AuthDto;
import com.ssafy.d103.auth.exception.ResourceNotFoundException;
import com.ssafy.d103.auth.model.AuthEntity;
import com.ssafy.d103.auth.model.MemberEntity;
import com.ssafy.d103.auth.repository.UserRepository;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.UserPrincipal;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Api(tags = {"user"})
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @param userPrincipal
     * @return
     */
//    @Api()
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public MemberEntity getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    /**
     *
     * @param userPrincipal
     * @return
     */
//    @GetMapping("/user/auth")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<?> getUserAuth(@CurrentUser UserPrincipal userPrincipal){
//        Optional<MemberEntity> member = userRepository.findById(userPrincipal.getId());
//        List<AuthEntity> authList = member.get().getAuth();
//
//        if(authList == null){
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        }
//
//        List<AuthDto> authDtoList = new LinkedList<>();
//        for(AuthEntity authEntity : authList){
//            authDtoList.add(new AuthDto().builder()
//                                .access_token(authEntity.getAccess_token())
//                                .provider(authEntity.getAuth_provider())
//                                .build());
//        }
//
//        return new ResponseEntity(authDtoList, HttpStatus.OK);
//    }

}
