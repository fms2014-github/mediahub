package com.rest.api.controller.common;

import com.google.gson.Gson;
import com.rest.api.model.social.RetKakaoAuth;
import com.rest.api.service.social.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RestController
@RequestMapping("/social/login")
public class SocialController {
    private final Environment env;
    private final RestTemplate restTemplate;
    private final Gson gson;
    private final KakaoService kakaoService;
    @Value("${spring.url.base}")
    private String baseUrl;
    @Value("${spring.social.kakao.client_id}")
    private String kakaoClientId;
    @Value("${spring.social.kakao.redirect}")
    private String kakaoRedirect;
    /**
     * 카카오 로그인 페이지 url 넘겨주는 작업이 필요함
     */
    @GetMapping
    public String socialLogin() {
        StringBuilder loginUrl = new StringBuilder()
                .append(env.getProperty("spring.social.kakao.url.login"))
                .append("?client_id=").append(kakaoClientId)
                .append("&response_type=code")
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirect);
        return loginUrl.toString();
    }
    /**
     * 카카오 인증 완료 후 리다이렉트는 프론트로 해줘야합니다.
     * 임시로 서버가 받는 코드를 작성해놓았습니다.
     */
    @GetMapping(value = "/kakao")
    public ModelAndView redirectKakao(ModelAndView mav, @RequestParam String code) {
        RetKakaoAuth rka = kakaoService.getKakaoTokenInfo(code);
        mav.addObject("authInfo", rka);
        mav.setViewName("social/redirectKakao");
        return mav;
    }
}
