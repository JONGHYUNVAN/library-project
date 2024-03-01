package com.demo.library.security.jwt.jwtservice;

import com.demo.library.security.entity.RefreshToken;
import com.demo.library.security.jwt.jwttokenizer.JWTTokenizer;
import com.demo.library.security.repository.RefreshTokenRepository;
import com.demo.library.user.entity.User;
import com.demo.library.user.service.UserService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Setter
public class JWTService {
    private final JWTTokenizer jwtTokenizer;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    public String getEmail(String token){
        int i = token.lastIndexOf('.');
        String payload = token.substring(7, i+1);
        return jwtTokenizer.getSubjectFromPayload(payload);
    }
    public String refreshAccessToken(String refreshToken) {
        String email = jwtTokenizer.getClaims(refreshToken, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey())).getBody().getSubject();
        User user = userService.findByEmail(email);

        return jwtTokenizer.createAccessToken(user);
    }



    public String refreshRefreshToken(String oldRefreshToken) {
        String email = jwtTokenizer.getClaims(oldRefreshToken, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey())).getBody().getSubject();
        User user = userService.findByEmail(email);

        return createRefreshToken(user);
    }

    private String createRefreshToken(User user) {
        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String newToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        refreshTokenRepository.save(
                RefreshToken.builder().userEmail(subject).token(newToken).expiryDateTime(expiryDateTime).build()
        );


        return newToken;
    }
}
