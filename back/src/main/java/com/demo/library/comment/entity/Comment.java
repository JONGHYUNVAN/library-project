package com.demo.library.comment.entity;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.post.entity.PostEntity;
import com.demo.library.timeEntity.Auditable;
import com.demo.library.user.entity.User;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "Comments")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    @NotNull
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;
}
