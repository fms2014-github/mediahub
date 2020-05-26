package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.exception.ResourceNotFoundException;
import com.ssafy.d103.auth.model.MemberEntity;
import com.ssafy.d103.auth.repository.MemberRepository;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.UserPrincipal;
import com.ssafy.d103.auth.youtube.YouTubeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"user"})
@RestController
public class UserController {

    @Autowired
<<<<<<< HEAD
    private MemberRepository memberRepository;

    @Autowired
    private YouTubeService youTubeService;
=======
    private MemberRepository userRepository;
>>>>>>> feature/backend-twitch

    /**
     *
     * @param userPrincipal
     * @return
     */
//    @Api()
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public MemberEntity getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        System.out.println(youTubeService.getImplicitCodeFlowUrl());
        return memberRepository.findById(userPrincipal.getId())
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
