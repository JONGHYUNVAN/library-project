package com.demo.library.security.service;

import com.demo.library.security.utils.AuthorityUtils;
import com.demo.library.user.repository.UserRepository;
import com.demo.library.utils.EntityUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import com.demo.library.user.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.demo.library.user.entity.User.Status.ACTIVE;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final AuthorityUtils authorityUtils;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = user.getAttribute("sub");
        String email = null;
        if ("kakao".equals(provider)) {
            Map<String, Object> attributes = (Map<String, Object>) user.getAttributes().get("kakao_account");
            if (attributes != null) {
                email = (String) attributes.get("email");
            }
        }
            Optional<User> optionalUser = userRepository.findByProviderAndProviderId(provider, providerId);
            if (optionalUser.isEmpty()) {
                // 사용자 계정이 없는 경우, 새로운 계정을 생성합니다.
                User newUser = new User();
                newUser.setProvider(provider);
                newUser.setProviderId(providerId);
                newUser.setEmail(email);
                List<String> roles = authorityUtils.createRoles(email);
                newUser.setRoles(roles);
                newUser.setStatus(ACTIVE);

                userRepository.save(newUser);
            }

            return user;
    }
}
