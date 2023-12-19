package com.demo.library.security.filter;

import com.demo.library.security.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private static final String SIGNING_KEY = "your-signing-key";
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_TOKEN_PREFIX.length());

            try {
                Jws<Claims> claims = Jwts.parserBuilder()
                        .setSigningKey(SIGNING_KEY.getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(token);

                // 토큰에서 사용자 이메일을 추출합니다.
                String userEmail = claims.getBody().getSubject();

                // 사용자 이메일을 이용하여 사용자 정보를 불러옵니다.
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // 사용자 정보를 이용하여 Authentication 객체를 생성합니다.
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // 생성한 Authentication 객체를 SecurityContext 에 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                logger.info("Not OAuth2 Login", e);

            }
        }

        filterChain.doFilter(request, response);
    }
}
