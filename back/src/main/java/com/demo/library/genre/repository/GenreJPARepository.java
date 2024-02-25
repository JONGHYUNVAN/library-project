package com.demo.library.genre.repository;

import com.demo.library.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GenreJPARepository extends JpaRepository<Genre, Long> {
        Optional<Genre> findByName(String name);

}
