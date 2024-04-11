package com.demo.library.library.entity;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.loanNban.entity.Loan;
import lombok.*;
import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "libraries")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( nullable = false, length = 30)
    private String name;
    @Column(nullable = false, length = 250)
    private String address;
    @Column(nullable = false)
    private String openTime;
    @Column(nullable = false)
    private String closeTime;
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private List<BookEntity> books;
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private List<Loan> loans;


}
