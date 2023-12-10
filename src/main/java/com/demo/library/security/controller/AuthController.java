package com.demo.library.security.controller;

import com.demo.library.response.ResponseCreator;
import com.demo.library.response.dto.SingleResponseDto;
import com.demo.library.security.entity.RefreshToken;
import com.demo.library.security.jwt.dto.TokenDto;

import com.demo.library.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

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
        RefreshToken requestToken = authService.isValidRequest(tokenRequest);
        TokenDto.Set tokenSet =  authService.refresh(requestToken);
        return ResponseCreator.single(tokenSet);
    }
}
