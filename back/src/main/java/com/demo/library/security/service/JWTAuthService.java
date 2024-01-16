package com.demo.library.security.service;

import com.demo.library.exception.BusinessLogicException;
import com.demo.library.security.entity.RefreshToken;
import com.demo.library.security.jwt.dto.TokenDto;
import com.demo.library.security.jwt.jwtservice.JWTService;
import com.demo.library.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static com.demo.library.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTAuthService {
    private final JWTService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken isValidRequest(Cookie[] cookies){
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException(INVALID_TOKEN_REQUEST));

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(refreshToken);

        return optionalRefreshToken.orElseThrow(() ->
                new BusinessLogicException(REFRESH_TOKEN_NOT_FOUND));
    }

    public TokenDto.Set refresh(RefreshToken requestToken){
        TokenDto.Set tokenSet = new TokenDto.Set();
        tokenSet.setAccessToken(jwtService.refreshAccessToken(requestToken.getToken()));
        tokenSet.setRefreshToken(jwtService.refreshRefreshToken(requestToken.getToken()));
        refreshTokenRepository.delete(requestToken);
        return tokenSet;
    }
    public void checkIfExpired(RefreshToken refreshToken){
        if (isExpired(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            throw new BusinessLogicException(REFRESH_TOKEN_EXPIRED);
        }
    }
    public boolean isExpired(RefreshToken refreshToken){
        return refreshToken.getExpiryDateTime().isBefore(LocalDateTime.now());
    }
}
