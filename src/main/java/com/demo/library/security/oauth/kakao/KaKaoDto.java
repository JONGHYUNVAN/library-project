package com.demo.library.security.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
public class KaKaoDto {
    private String id;

    @JsonProperty("properties")
    private Properties properties;

    @Getter
    public static class Properties {

        private String nickname;

    }
}
