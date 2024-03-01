package com.demo.library.security.repository;

import com.demo.library.security.entity.RefreshToken;
import com.demo.library.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

public interface RefreshTokenJPARepository extends JpaRepository <RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteAllByExpiryDateTimeBefore(Date now);
    @Transactional
    void deleteByUserEmail(String email);
}
