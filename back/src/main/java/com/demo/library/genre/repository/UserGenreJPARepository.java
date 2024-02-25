package com.demo.library.genre.repository;

import com.demo.library.genre.entity.Genre;
import com.demo.library.genre.entity.UserGenre;
import com.demo.library.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGenreJPARepository extends JpaRepository<UserGenre, Long> {
    Optional<UserGenre> findAllByUserAndGenre(User user,Genre genre);
}
