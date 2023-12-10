package com.demo.library.security.service;

import com.demo.library.exception.BusinessLogicException;
import com.demo.library.security.entity.RefreshToken;
import com.demo.library.security.jwt.dto.TokenDto;
import com.demo.library.security.jwt.jwtservice.JWTService;
import com.demo.library.security.jwt.jwttokenizer.JWTTokenizer;
import com.demo.library.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.demo.library.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JWTService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken isValidRequest(TokenDto.Request tokenRequest){
        if (tokenRequest == null || tokenRequest.getRefreshToken()== null) {
            throw new BusinessLogicException(INVALID_TOKEN_REQUEST);
        }

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(tokenRequest.getRefreshToken());

        return optionalRefreshToken.orElseThrow(() ->
                new BusinessLogicException(INVALID_REFRESH_TOKEN));
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
