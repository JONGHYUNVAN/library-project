package com.demo.library.loanNban.service;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.book.service.BookService;
import com.demo.library.loanNban.entity.Loan;
import com.demo.library.loanNban.repository.LoanRepository;
import com.demo.library.exception.BusinessLogicException;
import com.demo.library.user.entity.User;
import com.demo.library.user.service.UserService;
import com.demo.library.utils.timeConverter.LocalDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Optional;

import static com.demo.library.exception.ExceptionCode.*;


@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository repository;
    private final BookService bookService;
    private final UserService userService;
    private final BanService banService;
    private final LocalDateTimeConverter localDateTimeConverter;
     
    public Loan create(Loan loan){
        BookEntity book = bookService.verifyById(loan.getBook().getId());
        User user = userService.verifyById(loan.getUser().getId());
        verifyByUserAndBook(user, book);

        bookService.loanBook(book);
        loan.setDueDate(LocalDateTime.now().plusDays(7));

        return repository.save(loan);
    }
     
    public Loan update(Loan loan) {
        Loan loanFromDB = findAndVerifyLoanById(loan.getId());
        LocalDateTime dueDate = loan.getDueDate();
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(3);

        if (isAfter(newDueDate,dueDate))
            loanFromDB.setDueDate(newDueDate);

        loanFromDB.setUpdatedAt(LocalDateTime.now());
        return loanFromDB;
    }

    public void delete(Long Id) {
       Loan loan = findAndVerifyLoanById(Id);

       BookEntity book = loan.getBook();
       setBookAvailable(book);

       createLog(loan);

       repository.delete(loan);
    }



    //inner Methods

    private static void setBookAvailable(BookEntity book) {
        book.setStatus(BookEntity.Status.AVAILABLE);
    }

    public void checkOverdueLoan(User user) {
        List<Loan> loans = user.getLoans();

        for (Loan loan : loans) {
            LocalDateTime dueDate = loan.getDueDate();
            LocalDateTime now = LocalDateTime.now();

            if (isOverDue(dueDate, now)) {
                banService.create(loan);
                throw new BusinessLogicException(HAS_OVERDUE_LOAN);
            }
        }
    }
    private static boolean isOverDue(LocalDateTime dueDate, LocalDateTime now) {
        return dueDate.isBefore(now);
    }

    private static boolean isAfter(LocalDateTime newDueDate,LocalDateTime dueDate) {
        return newDueDate.isAfter(dueDate);
    }
    private void verifyByUserAndBook(User user, BookEntity book) {
        bookService.ifOnLoan(book);
        banService.checkIfBanned(user, book.getLibrary());
        checkOverdueLoan(user);
    }

    public Loan findAndVerifyLoanById(Long Id) {
        Optional<Loan> optionalLoan = repository.findById(Id);

        return optionalLoan.orElseThrow(() ->
                new BusinessLogicException(LOAN_NOT_FOUND));
    }

    public List<Loan> findLoans (Long userId){
        User user = userService.verifyById(userId);

        return repository.findLoansByUser(user);
    }
    private void createLog(Loan loan){
        BookEntity book = loan.getBook();
        String log = loan.getUser().getId()
                   + " " + loan.getUser().getName()
                   + " " + loan.getCreatedAt()
                   + " " + LocalDateTime.now();

        book.getLogs().add(log);
    }
}