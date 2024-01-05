package com.demo.library.book.controller;


import com.demo.library.book.dto.BookDto;
import com.demo.library.book.entity.BookEntity;
import com.demo.library.book.repository.BookJPARepository;
import com.demo.library.book.repository.BookRepository;
import com.demo.library.library.entity.Library;
import com.demo.library.library.repository.LibraryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username="user", roles={"USER","ADMIN"})
public class PatchTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LibraryRepository libraryRepository;
    @BeforeEach
    void  setup() {
        Library existingLibrary = Library.builder()
                .id(1L)
                .name("existingName")
                .address("existingAddress")
                .openTime("9:30")
                .closeTime("17:30")


                .build();

        libraryRepository.save(existingLibrary);
        BookEntity setupBook = BookEntity.builder()
                .id(1L)
                .title("setupTitle")
                .publisher("setupPublisher")
                .author("setupAuthor")
                .status(BookEntity.Status.ON_LOAN)
                .library(existingLibrary)
                .build();

        bookRepository.save(setupBook);

    }
    @Test
    void patchBookTest() throws Exception {
        // Given
        String requestBody = getRequestBody(1L,"newTitle","newPublisher","newAuthor");

        // When
        ResultActions result = mockMvc.perform(
                patch("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("newTitle"))
                .andExpect(jsonPath("$.data.publisher").value("newPublisher"))
                .andExpect(jsonPath("$.data.author").value("newAuthor"));
    }

    @Test
    public void patchBookTest_Validation_titleNotBlank() throws Exception {
        BookDto.Patch patch = getPatch(1L,"","newPublisher","newAuthor");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        //Then
        getExpect(result,"title","","Title should not be null.");

    }

    @Test
    void patchBookTest_Validation_titleTooLong() throws Exception {
        // Given
        BookDto.Patch patch = getPatch(1L, "1234567891011121314151617181920", "newPublisher", "newAuthor");

        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        getExpect(result, "title", "1234567891011121314151617181920", "Title should be at most 20 characters.");
    }

    @Test
    public void patchBookTest_Validation_publisherNotBlank() throws Exception {
        BookDto.Patch patch = getPatch(1L, "title", "", "newAuthor");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        //Then
        getExpect(result, "publisher", "", "Publisher should not be null.");
    }
    @Test
    void postBookTest_Validation_publisherTooLong() throws Exception {
        // Given
        BookDto.Patch patch = getPatch(1L, "publisher", "012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "newAuthor");

        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        getExpect(result, "publisher", "012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "Publisher should be at most 20 characters.");
    }
    @Test
    public void patchBookTest_Validation_authorNotBlank() throws Exception {
        BookDto.Patch patch = getPatch(1L, "title", "publisher", "");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        //Then
        getExpect(result, "author","","Author should not be null.");
    }
    @Test
    void postBookTest_Validation_authorTooLong() throws Exception {
        // Given
        BookDto.Patch patch = getPatch(1L, "title", "publisher", "012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        getExpect(result, "author", "012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "Author should be at most 20 characters.");
    }
    private String getRequestBody(Long id, String title, String publisher, String author) throws JsonProcessingException {

        BookDto.Patch patch = getPatch(id, title, publisher, author);
        String requestBody = objectMapper.writeValueAsString(patch);
        return requestBody;
    }

    private static BookDto.Patch getPatch(Long id, String title, String publisher, String author) {
        BookDto.Patch patch = BookDto.Patch.builder()
                .id(id)
                .title(title)
                .publisher(publisher)
                .author(author)

                .build();
        return patch;
    }
    private static ResultActions getExpect(ResultActions result, String field, String rejectValue, String reason) throws Exception {
        return result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value(field))
                .andExpect(jsonPath("$.fieldErrors[0].rejectValue").value(rejectValue))
                .andExpect(jsonPath("$.fieldErrors[0].reason").value(reason));
    }
}
