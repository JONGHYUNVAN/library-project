package com.demo.library.book.controller;

import com.demo.library.book.dto.BookDto;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username="user", roles={"USER","ADMIN"})
public class PostTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LibraryRepository libraryRepository;
    @BeforeEach
    void  setup() {
        Library existingLibrary = Library.builder()
                .id(1L)
                .name("existingName")
                .address("existingAddress")
                .openTime("9:30")
                .closeTime("17,30")


                .build();

        libraryRepository.save(existingLibrary);
    }

    @Test
    void postBookTest() throws Exception {
        // Given
        String requestBody = getRequestBody();

        // When
        ResultActions result = getResult(requestBody);

        // Then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/books")));
    }


    @Test
    void postBookTest_Validation_titleNotBlank() throws Exception {
        // Given
        BookDto.Post post = getPost("",  "publisher",  "author", 1L);
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"title","","Title should not be null.");
    }

    @Test
    void postBookTest_Validation_titleTooLong() throws Exception {
        // Given
        BookDto.Post post = getPost("1234567891011121314151617181920", "publisher",  "author", 1L);
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"title","1234567891011121314151617181920","Title should be at most 20 characters.");
    }

    @Test
    void postBookTest_Validation_publisherNotBlank() throws Exception {
        // Given
        BookDto.Post post = getPost("title", "", "author", 1L);
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"publisher","","Publisher should not be null.");
    }

    @Test
    void postBookTest_Validation_publisherTooLong() throws Exception {
        // Given
        BookDto.Post post = getPost("title", "012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "author", 1L);
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"publisher","012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890","Publisher should be at most 20 characters.");
        }

    @Test
    void postBookTest_Validation_authorNotBlank() throws Exception {
        // Given
        BookDto.Post post = getPost("title", "publisher", "", 1L);
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"author","","Author should not be null.");
     }


    private static BookDto.Post getPost(String title, String publisher, String author, Long libraryId) {
        BookDto.Post post = BookDto.Post.builder()
                .title(title)
                .publisher(publisher)
                .author(author)
                .libraryId(libraryId)

                .build();
        ;
        return post;
    }
    private String getRequestBody() throws JsonProcessingException {
        BookDto.Post post = getPost("title",  "publisher",  "author", 1L);
        return objectMapper.writeValueAsString(post);
    }

    private ResultActions getResult(String requestBody) throws Exception {
        ResultActions result;
        result = mockMvc.perform(
                post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        return result;
    }
    private static void assertWith(ResultActions result, String field, String rejectValue, String reason) throws Exception {
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value(field))
                .andExpect(jsonPath("$.fieldErrors[0].rejectValue").value(rejectValue))
                .andExpect(jsonPath("$.fieldErrors[0].reason").value(reason));
    }


}


