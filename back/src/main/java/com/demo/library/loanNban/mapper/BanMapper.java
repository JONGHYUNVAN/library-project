package com.demo.library.loanNban.mapper;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.library.entity.Library;
import com.demo.library.loanNban.entity.Ban;
import com.demo.library.loanNban.entity.Loan;
import com.demo.library.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BanMapper {
    public Ban loanToBan (Loan loan){
        User user = loan.getUser();
        BookEntity book = loan.getBook();
        Library library = loan.getLibrary();


        return Ban.builder()
                .user(user)
                .book(book)
                .loan(loan)
                .library(library)

                .build();
    }

}
