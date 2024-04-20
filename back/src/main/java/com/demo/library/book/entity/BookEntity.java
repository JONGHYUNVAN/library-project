package com.demo.library.book.entity;

import com.demo.library.genre.entity.Genre;
import com.demo.library.post.entity.PostEntity;
import com.demo.library.timeEntity.Auditable;
import com.demo.library.library.entity.Library;
import com.demo.library.loanNban.entity.Loan;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(nullable = false, length = 30)
    private String publisher;
    @Column(nullable = false, length = 30)
    private String author;
    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Column(nullable = false, name = "book_status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Getter
    public enum Status{AVAILABLE, ON_LOAN}
    @OneToOne
    @JoinColumn(name = "loan")
    private Loan loan;
    @Column
    private Long searchCount;
    @Column
    private String imageURL;
    @OneToOne
    private PostEntity post;

    @ElementCollection
    @CollectionTable(name = "log", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "logs")
    private List<String> logs;


}
