package com.demo.library.security.oauth.google;

import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    private final GoogleProperties googleProperties;
    private final GoogleUserService googleUserService;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> parameters = getFromGoogleProperties(code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", request, String.class);

        if (isSucceeded(response)) {
            String responseBody = response.getBody();
            return getAccessTokenFromResponseBody(responseBody, new ObjectMapper());
        }
        else {
            printError(response);
            return null;
        }
    }

    private static void printError(ResponseEntity<String> response) {
        String error = "Request failed. HTTP Status Code: " + response.getStatusCode();
        System.err.println(error);
    }

    private static String getAccessTokenFromResponseBody(String responseBody, ObjectMapper mapper) {
        try {
            JsonNode root = mapper.readTree(responseBody);
            return root.path("access_token").asText();
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isSucceeded(ResponseEntity<String> response) {
        return response.getStatusCode() == HttpStatus.OK;
    }

    private MultiValueMap<String, String> getFromGoogleProperties(String code) {
        String clientId = googleProperties.getClientId();
        String clientSecret = googleProperties.getClientSecret();
        String redirectUri = googleProperties.getRedirectUri();
        String grantType = googleProperties.getAuthorizationGrantType();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_id", clientId);
        parameters.add("client_secret", clientSecret);
        parameters.add("redirect_uri", redirectUri);
        parameters.add("code", code);
        parameters.add("grant_type", grantType);
        return parameters;
    }

    public GoogleDto getUserInfo(String accessToken) {
        String requestUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(requestUrl, HttpMethod.GET, entity, GoogleDto.class).getBody();
    }

    public User authenticateUser(GoogleDto googleDto) {
        String email = googleDto.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        return optionalUser.orElseGet(() -> googleUserService.create(email, googleDto.getName(), googleDto.getId()));
    }
}