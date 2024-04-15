package com.demo.library.post.entity;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.timeEntity.Auditable;
import com.demo.library.user.entity.User;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "posts")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @Column
    private Long views;

    public PostEntity(Long id, String title, User user, BookEntity book, LocalDateTime createdAt, LocalDateTime updatedAt, Long views) {
        super(createdAt, updatedAt);
        this.id = id;
        this.title = title;
        this.user = user;
        this.book = book;
        this.views = views;
    }
}
