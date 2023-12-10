package com.demo.library.security.jwt.jwtservice;

import com.demo.library.security.entity.RefreshToken;
import com.demo.library.security.jwt.jwttokenizer.JWTTokenizer;
import com.demo.library.security.repository.RefreshTokenRepository;
import com.demo.library.user.entity.User;
import com.demo.library.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Setter
public class JWTService {
    private final JWTTokenizer jwtTokenizer;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    public String refreshAccessToken(String refreshToken) {
        String email = jwtTokenizer.getClaims(refreshToken, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey())).getBody().getSubject();
        User user = userService.findByEmail(email);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("userName", user.getName());
        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }
    public String refreshRefreshToken(String oldRefreshToken) {
        String email = jwtTokenizer.getClaims(oldRefreshToken, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey())).getBody().getSubject();
        User user = userService.findByEmail(email);


        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String newToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(newToken);
        refreshToken.setExpiryDateTime(expiryDateTime);
        refreshTokenRepository.save(refreshToken);

        return newToken;
    }
}
