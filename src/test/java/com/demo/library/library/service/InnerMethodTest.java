package com.demo.library.library.service;

import com.demo.library.exception.BusinessLogicException;
import com.demo.library.library.entity.Library;
import com.demo.library.library.repository.LibraryRepository;
import com.demo.library.utils.EntityUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

import static com.demo.library.exception.ExceptionCode.INVALID_LIBRARY_ID;
import static com.demo.library.exception.ExceptionCode.LIBRARY_ALREADY_EXISTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InnerMethodTest {
    private LibraryService libraryService;
    @Mock
    private LibraryRepository libraryRepository;
    @Mock
    private EntityUpdater<Library> entityUpdater;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        libraryService = new LibraryService(libraryRepository,entityUpdater);
    }

    @Test
    public void test_verifyById_validId() {
        Long libraryId = 1L;
        Library library = new Library();
        library.setId(libraryId);

        when(libraryRepository.findById(any(Long.class))).thenReturn(Optional.of(library));
        Library result = libraryService.verifyById(libraryId);

        assertEquals(library, result);
    }

    @Test
    public void test_verifyById_inValidId() {
        Long libraryId = 1L;

        when(libraryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
            libraryService.verifyById(libraryId);
        });

        assertEquals(INVALID_LIBRARY_ID, exception.getExceptionCode());
    }
    @Test
    public void test_isExistName_validName() {
        // Given
        String name = "libraryName";

        // When
        when(libraryRepository.findByName(name)).thenReturn(Optional.empty());

        // Then
        assertDoesNotThrow(() -> libraryService.isExistName(name));
    }
    @Test
    public void test_isExistName_inValidName() {
        // Given
        String name = "libraryName";
        Library library = new Library();
        library.setName(name);
        // When
        when(libraryRepository.findByName(name)).thenReturn(Optional.of(library));
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
            libraryService.isExistName(name);
        });
        // Then
        assertEquals(LIBRARY_ALREADY_EXISTS, exception.getExceptionCode());
    }
    @Test
    public void testIsValidRequest_WithValidId_NoExceptionThrown() {
        Long id = 1L;
        Library library = new Library();
        assertDoesNotThrow(() -> libraryService.verifyId(id));
    }

    @Test
    public void testIsValidRequest_WithNullId_ThrowsBusinessLogicException() {
        Long id = null;
        assertThrows(BusinessLogicException.class, () -> libraryService.verifyId(id));
    }


    @Test
    public void testGetLibrariesByKeyword_ReturnsPageOfLibraries() {
        String keyword = "java";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Library> dummyPage = Mockito.mock(Page.class);
        when(libraryRepository.findAllByNameContaining(any(String.class), any(Pageable.class))).thenReturn(dummyPage);

        Page<Library> result = libraryService.getLibrariesByKeyword(keyword, pageable);

        assertEquals(dummyPage, result);
    }
}