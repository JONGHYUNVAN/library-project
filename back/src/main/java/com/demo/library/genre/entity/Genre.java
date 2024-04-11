package com.demo.library.genre.entity;

import com.demo.library.book.entity.BookEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "genre")
    private List<BookEntity> books;

    @OneToMany
    private List<UserGenre> userGenres;

}
