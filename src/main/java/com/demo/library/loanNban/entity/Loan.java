package com.demo.library.loanNban.entity;


import com.demo.library.timeEntity.Auditable;
import com.demo.library.book.entity.BookEntity;
import com.demo.library.library.entity.Library;
import com.demo.library.user.entity.User;
import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "loans")
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookEntity book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private Library library;
    private LocalDateTime dueDate;

}
