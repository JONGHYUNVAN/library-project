package com.demo.library.loanNban.entity;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.library.entity.Library;
import com.demo.library.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
    private LocalDate endDate;
}
