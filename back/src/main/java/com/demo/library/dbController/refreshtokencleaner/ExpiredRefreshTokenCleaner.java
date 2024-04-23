package com.demo.library.dbController.refreshtokencleaner;

import com.demo.library.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class ExpiredRefreshTokenCleaner {
    private final RefreshTokenRepository refreshTokenRepository;
    @Scheduled(fixedDelay = 86400000)
    @Transactional
    public void deleteExpiredTokens(){
        refreshTokenRepository.deleteAllByExpiryDateTimeBefore(LocalDateTime.now());
    }
}
