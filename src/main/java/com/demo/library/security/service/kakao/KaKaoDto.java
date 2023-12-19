package com.demo.library.security.service.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
public class KaKaoDto {
    private long id;

    @JsonProperty("properties")
    private Properties properties;

    @Getter
    public static class Properties {

        private String nickname;

    }
}
