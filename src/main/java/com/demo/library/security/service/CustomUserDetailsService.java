package com.demo.library.security.service;

import com.demo.library.exception.BusinessLogicException;
import com.demo.library.exception.ExceptionCode;
import com.demo.library.security.utils.AuthorityUtils;
import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        Hibernate.initialize(optionalUser.get().getRoles());
        return new CustomUserDetails(findUser);
        }
        private final class CustomUserDetails extends User implements UserDetails {
                CustomUserDetails(User user) {
                setId(user.getId());
                setEmail(user.getEmail());
                setPassword(user.getPassword());
                Hibernate.initialize(user.getRoles());
                setRoles(user.getRoles());
            }
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorityUtils.createAuthorities(this.getRoles());
            }

            @Override
            public String getUsername() {
                return getEmail();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
    }

}
