package com.demo.library.security.oauth.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleDto {
    private String id;
    private String email;
    private String name;

}
