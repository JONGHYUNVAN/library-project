package com.demo.library.genre.service;

import com.demo.library.genre.entity.Genre;
import com.demo.library.genre.entity.UserGenre;
import com.demo.library.genre.repository.GenreRepository;
import com.demo.library.genre.repository.UserGenreRepository;
import com.demo.library.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserGenreService {
    private final UserGenreRepository userGenreRepository;
    private final GenreRepository genreRepository;

    public void generateUserGenres(User user){
        List<Genre> genres = genreRepository.findAll();
        List<UserGenre> userGenres = new ArrayList<>();
        for (Genre genre : genres) {
            UserGenre userGenre = new UserGenre(user, genre, 0L, 0L);
            userGenres.add(userGenre);
        }
        user.setUserGenres(userGenres);
        userGenreRepository.saveAll(userGenres);
    }
}
