package com.demo.library.book.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetBooksByKeyword() throws Exception {
        ResultActions result = mockMvc.perform(get("/books/keyword/앵무새")
                                      .contentType(MediaType.APPLICATION_JSON))
                                      .andExpect(status().isOk());

        result.andExpect(jsonPath("$.data[0].id").value(3))
                .andExpect(jsonPath("$.data[0].title").value("앵무새 죽이기"))
                .andExpect(jsonPath("$.data[0].publisher").value("Sunset Books"))
                .andExpect(jsonPath("$.data[0].author").value("Harper Lee"))
                .andExpect(jsonPath("$.data[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$.data[0].libraryId").value(1))
                .andExpect(jsonPath("$.data[0].libraryName").value("중앙도서관"));
    }
    @Test
    public void testGetBooksByAuthor() throws Exception {
        ResultActions result = mockMvc.perform(get("/books/author/Harper Lee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        result.andExpect(jsonPath("$.data[0].id").value(3))
                .andExpect(jsonPath("$.data[0].title").value("앵무새 죽이기"))
                .andExpect(jsonPath("$.data[0].publisher").value("Sunset Books"))
                .andExpect(jsonPath("$.data[0].author").value("Harper Lee"))
                .andExpect(jsonPath("$.data[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$.data[0].libraryId").value(1))
                .andExpect(jsonPath("$.data[0].libraryName").value("중앙도서관"));
    }
    @Test
    public void testGetBooksByPublisher() throws Exception {
        ResultActions result = mockMvc.perform(get("/books/publisher/Sunset Books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        result.andExpect(jsonPath("$.data[0].id").value(3))
                .andExpect(jsonPath("$.data[0].title").value("앵무새 죽이기"))
                .andExpect(jsonPath("$.data[0].publisher").value("Sunset Books"))
                .andExpect(jsonPath("$.data[0].author").value("Harper Lee"))
                .andExpect(jsonPath("$.data[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$.data[0].libraryId").value(1))
                .andExpect(jsonPath("$.data[0].libraryName").value("중앙도서관"));
    }

}
