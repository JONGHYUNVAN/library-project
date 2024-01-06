package com.demo.library.security.repository;


import com.demo.library.security.entity.RefreshToken;
import com.demo.library.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteAllByExpiryDateTimeBefore(Date now);

    void deleteByUser(User user);
}