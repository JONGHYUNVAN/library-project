package com.demo.library.loanNban.service;

import com.demo.library.exception.BusinessLogicException;
import com.demo.library.library.entity.Library;
import com.demo.library.loanNban.entity.Ban;
import com.demo.library.loanNban.entity.Loan;
import com.demo.library.loanNban.mapper.BanMapper;
import com.demo.library.loanNban.repository.BanRepository;
import com.demo.library.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.demo.library.exception.ExceptionCode.USER_BANNED_FROM_LIBRARY;


@Service
@RequiredArgsConstructor
public class BanService {
    private final BanRepository repository;
    private final BanMapper mapper;

    public Ban create (Loan loan) {
        Ban ban = mapper.loanToBan(loan);

        LocalDate endDate = getEndDate(loan.getDueDate());
        ban.setEndDate(endDate);

        return repository.save(ban);
    }
    public void refresh(User user){
        if (banExists(user)) {
            List<Ban> bans = user.getBans();
            LocalDate today = LocalDate.now();
            for (Ban ban : bans) {
                if (ban.getEndDate().isBefore(today))
                    repository.delete(ban);
            }
        }
    }
    private boolean banExists (User user) {
        List<Ban> bans = user.getBans();
        return bans != null && !bans.isEmpty();
    }
    public void checkIfBanned(User user, Library library) {
        refresh(user);
        if (isUserBanned(user, library)) {
            throw new BusinessLogicException(USER_BANNED_FROM_LIBRARY);
        }
    }
    private boolean isUserBanned(User user, Library library) {
        List<Ban> bans= user.getBans();
        return bans.stream()
                .anyMatch(ban -> ban.getLibrary().equals(library));
    }
    private LocalDate getEndDate(LocalDateTime dueDate) {
        LocalDate today = LocalDate.now();
        long overdueDays = getOverdueDays(dueDate, today);
        LocalDate endDate = today.plusDays(overdueDays);
        return endDate;
    }

    private static long getOverdueDays(LocalDateTime dueDate, LocalDate today) {
        return ChronoUnit.DAYS.between(dueDate.toLocalDate(), today);
    }

}
