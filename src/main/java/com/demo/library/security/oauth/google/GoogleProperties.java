package com.demo.library.security.oauth.google;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")@Getter
@Setter
public class GoogleProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizationGrantType;
    private String scope;
}
