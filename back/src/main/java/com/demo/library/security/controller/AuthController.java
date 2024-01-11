package com.demo.library.security.controller;

import com.demo.library.response.ResponseCreator;
import com.demo.library.response.dto.SingleResponseDto;
import com.demo.library.security.entity.RefreshToken;
import com.demo.library.security.jwt.dto.TokenDto;

import com.demo.library.security.oauth.google.GoogleAuthService;
import com.demo.library.security.oauth.google.GoogleDto;
import com.demo.library.security.oauth.google.GoogleJWTService;
import com.demo.library.security.service.JWTAuthService;
import com.demo.library.security.oauth.kakao.KaKaoDto;
import com.demo.library.security.oauth.kakao.KakaoAuthService;
import com.demo.library.security.oauth.kakao.KakaoJWTService;
import com.demo.library.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JWTAuthService JWTAuthService;
    private final KakaoAuthService kakaoAuthService;
    private final KakaoJWTService kakaoJWTService;
    private final GoogleAuthService googleAuthService;
    private final GoogleJWTService googleJWTService;


    @GetMapping("/login-form")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access denied";
    }

    @PostMapping("/login")
    public String login() {
        return "home";
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<SingleResponseDto<TokenDto.Set>> refreshAccessToken(@RequestBody TokenDto.Request tokenRequest) {
        RefreshToken requestToken = JWTAuthService.isValidRequest(tokenRequest);
        JWTAuthService.checkIfExpired(requestToken);
        TokenDto.Set tokenSet =  JWTAuthService.refresh(requestToken);
        return ResponseCreator.single(tokenSet);
    }
    @GetMapping("/oauth2/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        KaKaoDto kaKaoDto = kakaoAuthService.getUserInfo(accessToken);
        User user = kakaoAuthService.authenticateUser(kaKaoDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoJWTService.generateAccessToken(user));
        headers.add("Refresh",kakaoJWTService.generateRefreshToken(user));

        return new ResponseEntity<>("Kakao Authentication succeeded", headers, HttpStatus.OK);
    }
    @GetMapping("/oauth2/google")
    public ResponseEntity<String> googleCallback(@RequestParam("code") String code) {
        String accessToken = googleAuthService.getAccessToken(code);
        GoogleDto googleDto = googleAuthService.getUserInfo(accessToken);
        User user = googleAuthService.authenticateUser(googleDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleJWTService.generateAccessToken(user));
        headers.add("Refresh",googleJWTService.generateRefreshToken(user));

        return new ResponseEntity<>("Google Authentication succeeded", headers, HttpStatus.OK);
    }
}
