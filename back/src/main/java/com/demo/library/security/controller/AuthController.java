package com.demo.library.security.controller;

import com.demo.library.response.ResponseCreator;
import com.demo.library.response.dto.SingleResponseDto;
import com.demo.library.security.entity.RefreshToken;
import com.demo.library.security.handler.CustomLogoutHandler;
import com.demo.library.security.jwt.dto.TokenDto;

import com.demo.library.security.jwt.jwttokenizer.JWTTokenizer;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JWTAuthService JWTAuthService;
    private final JWTTokenizer jwtTokenizer;
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
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        new CustomLogoutHandler( JWTAuthService).onLogoutSuccess(request, response, authentication);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/cookie/test")
    public  ResponseEntity<String> testCookie(HttpServletResponse response) {
        jwtTokenizer.setAsCookie("testcookiecookiechococookiecookiecookiechococookiecookiecookiechococookie",response);
        return new ResponseEntity<>("Token has been refreshed.", HttpStatus.OK);
    }

    @PostMapping("/token/refresh")
    public  ResponseEntity<String> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        RefreshToken requestToken = JWTAuthService.isValidRequest(cookies);
        JWTAuthService.checkIfExpired(requestToken);
        TokenDto.Set tokenSet =  JWTAuthService.refresh(requestToken);

        jwtTokenizer.setAsCookie(tokenSet.getRefreshToken(),response);

        response.setHeader("Authorization", "Bearer " + tokenSet.getAccessToken());

        return new ResponseEntity<>("Token has been refreshed.", HttpStatus.OK);
    }
    @GetMapping("/oauth2/kakao/code")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        KaKaoDto kaKaoDto = kakaoAuthService.getUserInfo(accessToken);
        User user = kakaoAuthService.authenticateUser(kaKaoDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoJWTService.generateAccessToken(user));

        jwtTokenizer.setAsCookie(kakaoJWTService.generateRefreshToken(user),response);

        return new ResponseEntity<>("Kakao Authentication succeeded", headers, HttpStatus.OK);
    }

    @GetMapping("/oauth2/kakao/token")
    public ResponseEntity<String> kakaoGetInfo(@RequestHeader("Authorization") String accessToken, HttpServletResponse response) {
        KaKaoDto kaKaoDto = kakaoAuthService.getUserInfo(accessToken);
        User user = kakaoAuthService.authenticateUser(kaKaoDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoJWTService.generateAccessToken(user));

        jwtTokenizer.setAsCookie(kakaoJWTService.generateRefreshToken(user),response);

        return new ResponseEntity<>("Kakao Authentication succeeded", headers, HttpStatus.OK);
    }
    @GetMapping("/oauth2/google/code")
    public ResponseEntity<String> googleCallback(@RequestParam("code") String code, HttpServletResponse response) {
        String accessToken = googleAuthService.getAccessToken(code);
        GoogleDto googleDto = googleAuthService.getUserInfo(accessToken);
        User user = googleAuthService.authenticateUser(googleDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleJWTService.generateAccessToken(user));

        jwtTokenizer.setAsCookie(googleJWTService.generateRefreshToken(user),response);

        return new ResponseEntity<>("Google Authentication succeeded", headers, HttpStatus.OK);
    }
    @GetMapping("/oauth2/google/token")
    public ResponseEntity<String> googleGetInfo(@RequestHeader("Authorization") String accessToken, HttpServletResponse response) {
        GoogleDto googleDto = googleAuthService.getUserInfo(accessToken);
        User user = googleAuthService.authenticateUser(googleDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleJWTService.generateAccessToken(user));

        jwtTokenizer.setAsCookie(googleJWTService.generateRefreshToken(user),response);

        return new ResponseEntity<>("Google Authentication succeeded", headers, HttpStatus.OK);
    }
}
