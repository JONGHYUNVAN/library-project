package com.demo.library.user.repository;

import com.demo.library.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.mockito.Mockito;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoryTest {
    @Autowired
    private  UserRepository userRepository;
    @MockBean
    private UserRepository mockUserRepository;


    @Test
    public void findById_WithValidId_ShouldReturnUser() {
        // Given
        User user = new User();
        user.setId(1L);
        Mockito.when(mockUserRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userRepository.findById(1L);

        // Then
        assertEquals(Optional.of(user), result);
    }

    @Test
    public void findByPhoneNumber_WithValidPhoneNumber_ShouldReturnUser() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setPhoneNumber("010-1234-5678");
        Mockito.when(mockUserRepository.findByPhoneNumber(eq("010-1234-5678"))).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userRepository.findByPhoneNumber("010-1234-5678");

        // Then
        assertEquals(Optional.of(user), result);
    }

}
