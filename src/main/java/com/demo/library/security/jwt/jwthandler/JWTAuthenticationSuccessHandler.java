package com.demo.library.security.jwt.jwthandler;


import com.demo.library.security.entity.RefreshToken;
import com.demo.library.security.jwt.jwttokenizer.JWTTokenizer;
import com.demo.library.security.repository.RefreshTokenRepository;
import com.demo.library.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JWTTokenizer jwtTokenizer;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Date refreshTokenExpiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(jwtTokenizer.getRefreshTokenExpirationMinutes());

        String subject;
        subject = user.getEmail(); // 사용자 이메일을 사용

        // 리프레시 토큰을 생성
        String refreshToken = jwtTokenizer.generateRefreshToken(
                subject, // 사용자 ID 등을 주제로 사용할 수 있습니다.
                refreshTokenExpiration,
                jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey())
        );

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(refreshToken);
        token.setExpiryDateTime(expiryDateTime);

        refreshTokenRepository.save(token);
        log.info("# Authentication succeeded");
    }
}
