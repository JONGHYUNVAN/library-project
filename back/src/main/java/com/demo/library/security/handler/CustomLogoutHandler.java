package com.demo.library.security.handler;

import com.demo.library.security.service.JWTAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutSuccessHandler {
    private final JWTAuthService jwtAuthService;
    HttpStatus httpStatusToReturn = HttpStatus.OK;
    @Override
    @Transactional
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        Optional.ofNullable(jwtAuthService.getEmail())
                .ifPresent(jwtAuthService::deleteRefreshTokenByEmail);
        response.setStatus(this.httpStatusToReturn.value());
        response.getWriter().flush();
    }
}
