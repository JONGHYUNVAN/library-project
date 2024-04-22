package com.demo.library.security.config;

import com.demo.library.security.filter.JwtAuthenticationFilter;
import com.demo.library.security.filter.JwtVerificationFilter;

import com.demo.library.security.handler.UserAccessDeniedHandler;
import com.demo.library.security.handler.UserAuthenticationEntryPoint;
import com.demo.library.security.jwt.jwthandler.JWTAuthenticationFailureHandler;
import com.demo.library.security.jwt.jwthandler.JWTAuthenticationSuccessHandler;
import com.demo.library.security.jwt.jwttokenizer.JWTTokenizer;
import com.demo.library.security.repository.RefreshTokenRepository;
import com.demo.library.security.utils.AuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {
    private final JWTTokenizer jwtTokenizer;
    private final AuthorityUtils authorityUtils;
    private final RefreshTokenRepository refreshTokenRepository;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 브라우저가 동일 출처에서만 iframe 을 렌더링하도록 설정
                .headers().frameOptions().sameOrigin()

                .and()
                .csrf().disable()// JWT 사용하므로 csrf 보호 비활성화
                .formLogin().disable()// 폼 기반 로그인 비활성화
                .httpBasic().disable()//  HTTP Basic 인증을 비활성화
                // cors 설정 커스텀화
               // .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
               // cors 디폴트 설정
                .cors(withDefaults());
        httpSecurity
                // 세션 생성 정책을 Stateless 로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 인증 실패시 사용할 핸들러
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new UserAuthenticationEntryPoint())
                .accessDeniedHandler(new UserAccessDeniedHandler())
                // 필터체인 커스텀화
                .and()
                .apply(new CustomFilterConfigurer())
                //인가
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/h2/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/users/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .antMatchers(HttpMethod.GET,"/users/my").authenticated()
                        .antMatchers(HttpMethod.PATCH,"/users/me").authenticated()
                        .antMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/users/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.POST, "/posts/**").authenticated()
                        .antMatchers(HttpMethod.GET,"/posts/my").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/posts/**").authenticated()
                        .antMatchers(HttpMethod.DELETE, "/posts/**").authenticated()
                        .antMatchers(HttpMethod.POST, "/comments/**").authenticated()
                        .antMatchers(HttpMethod.GET,"/comments/my").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/comments/**").authenticated()
                        .antMatchers(HttpMethod.DELETE, "/comments/**").authenticated()
                        .antMatchers(HttpMethod.POST, "/**/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/**/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/**/**").hasRole("ADMIN")
                        .antMatchers("/loans/**").authenticated()
                        .anyRequest().permitAll()

                );

        return httpSecurity.build();
    }
    @Configuration
    @EnableWebMvc
    public static class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000","https://library-project-blush.vercel.app")
                    .allowedMethods("GET", "POST","PATCH","DELETE","OPTIONS") // 허용할 HTTP method
                    .exposedHeaders("*") //header 노출
                    .allowedHeaders("*") // 요청 헤더 중 서버에서 허용하는 헤더
                    .allowCredentials(true) // 쿠키 인증 요청 허용
                    .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new JWTAuthenticationSuccessHandler(jwtTokenizer, refreshTokenRepository));
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }
}
