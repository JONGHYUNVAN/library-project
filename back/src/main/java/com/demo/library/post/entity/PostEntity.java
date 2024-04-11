package com.demo.library.post.entity;

import com.demo.library.timeEntity.Auditable;
import com.demo.library.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( nullable = false, length = 30)
    private String title;
    @Lob
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
