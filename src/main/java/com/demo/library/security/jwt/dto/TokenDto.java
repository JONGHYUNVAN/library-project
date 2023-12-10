package com.demo.library.security.jwt.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class TokenDto {
    @Getter
    public static class Request{
        @NotNull
        private String refreshToken;
    }
    @Getter
    @Setter
    public static class Set {
        @NotNull
        private String accessToken;
        @NotNull
        private String refreshToken;
    }
}
