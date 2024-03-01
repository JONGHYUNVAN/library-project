package com.demo.library.genre.service;

import com.demo.library.genre.entity.Genre;
import com.demo.library.genre.entity.UserGenre;
import com.demo.library.genre.repository.GenreRepository;
import com.demo.library.genre.repository.UserGenreRepository;
import com.demo.library.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserGenreService {
    private final UserGenreRepository userGenreRepository;
    private final GenreRepository genreRepository;
    public User generateUserGenres(User user){
        List<Genre> genres = genreRepository.findAll();
        List<UserGenre> userGenres = new ArrayList<>();
        for(Genre genre: genres){
           userGenres.add(userGenreRepository.save(new UserGenre(user,genre,0L,0L))) ;
        }
        user.setUserGenres(userGenres);
        return user;
    }
}
