package com.demo.library.user.service;


import com.demo.library.exception.BusinessLogicException;
import com.demo.library.genre.entity.Genre;
import com.demo.library.genre.entity.UserGenre;
import com.demo.library.genre.repository.UserGenreRepository;
import com.demo.library.genre.service.UserGenreService;
import com.demo.library.security.utils.AuthorityUtils;
import com.demo.library.user.dto.UserDto;
import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import com.demo.library.utils.EntityUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import static com.demo.library.exception.ExceptionCode.*;
import static com.demo.library.user.entity.User.Status.ACTIVE;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final EntityUpdater<User> entityUpdater;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityUtils authorityUtils;
    private final UserGenreRepository userGenreRepository;
    private final UserGenreService userGenreService;



    public User create(User user) {
        verifyExistUser(user);
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        List<String> roles = authorityUtils.createRoles(user.getEmail());
        user.setRoles(roles);
        user.setStatus(ACTIVE);
        save(user);
        userGenreService.generateUserGenres(user);

        return user;
    }

    public User update(User user) {
        Long userId = user.getId();
        User verifiedUser = verifyById(userId);
        String password = user.getPassword();
        if(password != null){
            if(password.length()>20||password.length()<8){
                throw new BusinessLogicException(INVALID_PASSWORD);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        entityUpdater.update(user, verifiedUser, User.class);

        save(verifiedUser);
        return verifiedUser;
    }

    public User find(UserDto.Request request) {
        Optional<User> optionalUser = repository.findByPhoneNumber(request.getPhoneNumber());
        return optionalUser.orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
    }
    public User findByEmail(String email){
        Optional<User> optionalUser = repository.findByEmail(email);
        return optionalUser.orElseThrow(() ->
                new BusinessLogicException(INVALID_USER_EMAIL));
    }

    public void delete(Long Id) {
        User verifiedUser = verifyById(Id);
        ifLoaning(verifiedUser);

        repository.delete(verifiedUser);
    }

    //inner methods
    public void isValidRequest(Long Id){
        Optional.ofNullable(Id)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
    }

    public void save(User user) {
        repository.save(user);
    }


    public User verifyById(long Id) {
        Optional<User> optionalUser = repository.findById(Id);

        return optionalUser.orElseThrow(() ->
                new BusinessLogicException(INVALID_USER_ID));
    }

    public User verifyByPhoneNumber(String phoneNumber){
        Optional<User> optionalUser = repository.findByPhoneNumber(phoneNumber);

        return optionalUser.orElseThrow(() ->
                new BusinessLogicException(INVALID_USER_PHONE_NUMBER));
    }

    public void verifyExistUser(User user) {
        Optional<User> foundUser = repository.findByPhoneNumber(user.getPhoneNumber());

        if (foundUser.isPresent())
            throw new BusinessLogicException(USER_ALREADY_EXISTS);
    }
    public void ifLoaning (User user) {
        if(!user.getLoans().isEmpty())
            throw new BusinessLogicException(LOAN_EXISTS);
    }

    public void updateUserGenreSearch(String email, Genre genre) {
        User user = findByEmail(email);
        UserGenre userGenre = userGenreRepository.findAllByUserAndGenre(user, genre)
                .orElseGet(() -> userGenreRepository.save(new UserGenre(user, genre, 0L, 0L)));

        userGenre.setSearched(userGenre.getSearched() + 1L);
        userGenreRepository.save(userGenre);
    }

    public void updateUserGenreLoan(User user, Genre genre) {
        UserGenre userGenre = userGenreRepository.findAllByUserAndGenre(user, genre)
                .orElseGet(() -> userGenreRepository.save(new UserGenre(user, genre, 0L, 0L)));

        userGenre.setLoaned(userGenre.getLoaned() + 1L);
        userGenreRepository.save(userGenre);
    }
}