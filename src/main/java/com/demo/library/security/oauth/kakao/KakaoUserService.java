package com.demo.library.security.oauth.kakao;

import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoUserService {
    private final UserRepository userRepository;
    public User create(String email, String nickName, String id){
        User user = User.builder()
                        .email(email)
                        .nickName(nickName)
                        .provider("kakao")
                        .providerId(id)
                        .name("kakao "+ nickName)
                        .phoneNumber("010-0000-0000")
                        .status(User.Status.ACTIVE)
                        .roles(List.of("USER"))
                        .gender(User.Gender.OAUTH)

                .build();
       return userRepository.save(user);
    }
}
