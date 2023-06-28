package com.demo.library.library.repository;

import com.demo.library.library.entity.Library;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoryTest {
    @Autowired
    private LibraryRepository libraryRepository;
    @MockBean
    private LibraryRepository mockUserRepository;


    @Test
    public void findById_WithValidId() {
        // Given
        Library library = new Library();
        library.setId(1L);
        Mockito.when(mockUserRepository.findById(eq(1L))).thenReturn(Optional.of(library));

        // When
        Optional<Library> result = libraryRepository.findById(1L);

        // Then
        assertEquals(Optional.of(library), result);
    }

    @Test
    public void findByPhoneName_WithValidName() {
        // Given
        Library library = new Library();
        library.setId(1L);
        library.setName("library");
        Mockito.when(mockUserRepository.findByName(eq("library"))).thenReturn(Optional.of(library));

        // When
        Optional<Library> result = libraryRepository.findByName("library");

        // Then
        assertEquals(Optional.of(library), result);
    }

}
