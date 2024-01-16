package com.demo.library.security.handler;

import com.demo.library.security.repository.RefreshTokenRepository;
import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutSuccessHandler {
    private final RefreshTokenRepository refreshTokenRepository;
    public final UserRepository userRepository;
    HttpStatus httpStatusToReturn = HttpStatus.OK;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // repository 에서 refreshToken 삭제
        if (authentication != null)
            userRepository.findByName(authentication.getName()).ifPresent(refreshTokenRepository::deleteByUser);

        response.setStatus(this.httpStatusToReturn.value());
        response.getWriter().flush();
    }
}
