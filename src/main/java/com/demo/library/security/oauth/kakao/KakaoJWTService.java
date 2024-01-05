package com.demo.library.security.oauth.kakao;

import com.demo.library.security.jwt.jwttokenizer.JWTTokenizer;
import com.demo.library.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KakaoJWTService {
    private final JWTTokenizer jwtTokenizer;
    public String generateAccessToken(User user) {return jwtTokenizer.createAccessToken(user);}
    public String generateRefreshToken(User user) {return jwtTokenizer.createRefreshToken(user);}

}
