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

import static com.demo.library.exception.ExceptionCode.INVALID_REFRESH_TOKEN;
import static com.demo.library.exception.ExceptionCode.INVALID_TOKEN_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JWTTokenizer jwtTokenizer;
    private final JWTService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken isValidRequest(TokenDto.Request tokenRequest){
        if (tokenRequest == null || tokenRequest.getRefreshToken()== null) {
            throw new BusinessLogicException(INVALID_TOKEN_REQUEST);
        }
        if (!jwtTokenizer.isValidToken(tokenRequest.getRefreshToken())) {
            throw (new BusinessLogicException(INVALID_REFRESH_TOKEN));
        }
    return refreshTokenRepository.findByToken(tokenRequest.getRefreshToken()).get();
    }

    public TokenDto.Set refresh(RefreshToken requestToken){
        TokenDto.Set tokenSet = new TokenDto.Set();
        tokenSet.setAccessToken(jwtService.refreshAccessToken(requestToken.getToken()));
        tokenSet.setRefreshToken(jwtService.refreshRefreshToken(requestToken.getToken()));
        refreshTokenRepository.delete(requestToken);
        return tokenSet;
    }
}
