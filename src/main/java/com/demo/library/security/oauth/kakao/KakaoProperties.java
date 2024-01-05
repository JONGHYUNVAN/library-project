package com.demo.library.security.oauth.kakao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")@Getter
@Setter
public class KakaoProperties {
    private String clientId;
    private String clientAuthenticationMethod;
    private String redirectUri;
    private String authorizationGrantType;
    private String scope;
}
