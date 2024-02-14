package com.demo.library.security.handler;

import com.demo.library.security.entity.RefreshToken;
import com.demo.library.security.repository.RefreshTokenRepository;
import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutSuccessHandler {
    private final RefreshTokenRepository refreshTokenRepository;
    public final UserRepository userRepository;
    HttpStatus httpStatusToReturn = HttpStatus.OK;
    @Override
    @Transactional
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        Cookie refreshTokenCookie = WebUtils.getCookie(request, "refreshToken");
        if(refreshTokenCookie != null) {
            String token = refreshTokenCookie.getValue();
            Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);
            refreshTokenOptional.ifPresent(refreshTokenRepository::delete);
        }
        response.setStatus(this.httpStatusToReturn.value());
        response.getWriter().flush();
    }
}
