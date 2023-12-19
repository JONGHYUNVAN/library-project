package com.demo.library.dbController.refreshtokencleaner;

import com.demo.library.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class ExpiredRefreshTokenCleaner {
    private final RefreshTokenRepository refreshTokenRepository;
    @Scheduled(fixedDelay = 86400000)
    public void deleteExpiredTokens(){
        refreshTokenRepository.deleteAllByExpiryDateTimeBefore(Calendar.getInstance().getTime());
    }
}
