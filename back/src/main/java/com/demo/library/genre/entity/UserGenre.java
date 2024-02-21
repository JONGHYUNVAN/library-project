package com.demo.library.genre.entity;
import com.demo.library.user.entity.User;
import lombok.*;
import javax.persistence.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Genre genre;
    @Column
    private Long searched;
    @Column
    private Long loaned;

    public UserGenre(User user, Genre genre, Long searched, Long loaned) {
        this.setUser(user);
        this.setGenre(genre);
        this.setSearched(searched);
        this.setLoaned(loaned);
    }
}