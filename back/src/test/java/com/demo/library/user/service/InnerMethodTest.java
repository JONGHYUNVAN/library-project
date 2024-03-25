package com.demo.library.user.service;

import com.demo.library.exception.BusinessLogicException;
import com.demo.library.genre.entity.UserGenre;
import com.demo.library.genre.repository.UserGenreRepository;
import com.demo.library.genre.service.UserGenreService;
import com.demo.library.loanNban.entity.Loan;
import com.demo.library.security.utils.AuthorityUtils;
import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import com.demo.library.utils.EntityUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static com.demo.library.exception.ExceptionCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InnerMethodTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private EntityUpdater<User> entityUpdater;
    private PasswordEncoder passwordEncoder;
    private AuthorityUtils authorityUtils;
    @Mock
    private UserGenreRepository userGenreRepository;
    private UserGenreService userGenreService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository,entityUpdater,passwordEncoder,authorityUtils,userGenreRepository,userGenreService);
    }

    @Test
    void test_verifyById_validId() {
        //Given
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        //When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        User result = userService.verifyById(userId);
        //Then
        assertEquals(user, result);
    }

    @Test
    void test_verifyById_invalidId() {
        //Given
        long userId = 1L;
        //When
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
            userService.verifyById(userId);
        });
        //Then
        assertEquals(INVALID_USER_ID, exception.getExceptionCode());
    }

    @Test
    void test_verifyByPhoneNumber_validPhoneNumber() {
        //Given
        String phoneNumber = "010-1234-5678";
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        //When
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));

        User result = userService.verifyByPhoneNumber(phoneNumber);
        //Then
        assertEquals(user, result);
    }

    @Test
    void test_verifyByPhoneNumber_invalidPhoneNumber() {
        //Given
        String phoneNumber = "1234567890";
        //When
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
            userService.verifyByPhoneNumber(phoneNumber);
                 });
        //Then
        assertEquals(INVALID_USER_PHONE_NUMBER, exception.getExceptionCode());
    }

    @Test
    void test_verifyExistUser_userExist() {
        //Given
        String phoneNumber = "1234567890";
        User existingUser = new User();
        existingUser.setPhoneNumber(phoneNumber);

        User newUser = new User();
        newUser.setPhoneNumber(phoneNumber);
        //When
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(existingUser));
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
            userService.verifyExistUser(newUser);
        });
        //Then
        assertEquals(USER_ALREADY_EXISTS, exception.getExceptionCode());
    }

    @Test
    void test_verifyExistUser_userNotExist() {
        //Given
        String phoneNumber = "1234567890";
        User newUser = new User();
        newUser.setPhoneNumber(phoneNumber);
        //When
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        //Then
        assertDoesNotThrow(() -> {
            userService.verifyExistUser(newUser);
        });
    }
    @Test
    void test_ifLoaning_LoanExist() {
        //Given
        User user = new User();
        user.setLoans(Collections.singletonList(new Loan()));
        //Then
        assertThrows(BusinessLogicException.class, () -> {
            userService.ifLoaning(user);
        });
    }
    @Test
    void test_ifLoaning_LoanNotExist() {
        //Given
        User user = new User();
        user.setLoans(Collections.emptyList());
        //Then
        assertDoesNotThrow(() -> {
            userService.ifLoaning(user);
        });
    }



}