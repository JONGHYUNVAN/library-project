package com.demo.library.security.repository;


import com.demo.library.security.entity.RefreshToken;
import com.demo.library.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface RefreshTokenRepository extends RefreshTokenJPARepository {

}