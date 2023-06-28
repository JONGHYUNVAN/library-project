package com.demo.library.book.repository;


import com.demo.library.book.entity.BookEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoryTest {
    @Autowired
    private BookJPARepository bookRepository;
    @MockBean
    private BookJPARepository mockUserRepository;



    @Test
    public void findById() {
        // Given
        BookEntity book = new BookEntity();
        book.setId(1L);
        Mockito.when(mockUserRepository.findById(eq(1L))).thenReturn(Optional.of(book));

        // When
        Optional<BookEntity> result = bookRepository.findById(1L);

        // Then
        assertEquals(Optional.of(book), result);
    }
    @Test
    public void findByTitle() {
        // Given
        BookEntity book = new BookEntity();
        book.setTitle("title");
        Mockito.when(mockUserRepository.findByTitle(eq("title"))).thenReturn(Optional.of(book));

        // When
        Optional<BookEntity> result = bookRepository.findByTitle("title");

        // Then
        assertEquals(Optional.of(book), result);
    }


}
