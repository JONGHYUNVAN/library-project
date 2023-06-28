package com.demo.library.loanNban.mapper;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.book.service.BookService;
import com.demo.library.loanNban.dto.LoanDto;
import com.demo.library.loanNban.entity.Loan;
import com.demo.library.loanNban.repository.LoanRepository;
import com.demo.library.user.entity.User;
import com.demo.library.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanMapper {
    public final LoanRepository repository;
    private final UserService userService;
    private final BookService bookService;
    public Loan postToLoan(LoanDto.Post post) {

        User user = userService.verifyById(post.getUserId());
        BookEntity book = bookService.verifyById(post.getBookId());

        return  Loan.builder()
                .user(user)
                .book(book)

                .build();
    }

    public Loan patchToLoan(LoanDto.Patch patch) {

        return repository.findById(patch.getId()).get();
    }

    public LoanDto.Response loanToResponse(Loan loan) {

        return LoanDto.Response.builder()

                .id(loan.getId())

                .bookId(loan.getBook().getId())
                .bookTitle(loan.getBook().getTitle())

                .userId(loan.getUser().getId())
                .userName(loan.getUser().getName())

                .dueDate(loan.getDueDate())
                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())

                .build();
    }
    public List<LoanDto.Response> loansToLoanResponses(List<Loan> loans) {
        return loans.stream()
                .map(loan -> {
                    LoanDto.Response response = new LoanDto.Response();
                    response.setId(loan.getId());

                    response.setBookId(loan.getBook().getId());
                    response.setBookTitle(loan.getBook().getTitle());

                    response.setUserId(loan.getUser().getId());
                    response.setUserName(loan.getUser().getName());

                    response.setDueDate(loan.getDueDate());
                    response.setCreatedAt(loan.getCreatedAt());
                    response.setUpdatedAt(loan.getUpdatedAt());

                    return response;
                })
                .collect(Collectors.toList());
    }

}