package com.demo.library.library.controller;

import com.demo.library.library.dto.LibraryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


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

    @Test
    void postLibraryTest() throws Exception {
        // Given
        String requestBody = getRequestBody();

        // When
        ResultActions result = getResult(requestBody);

        // Then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/libraries")));
    }


    @Test
    void postLibraryTest_Validation_nameNotBlank() throws Exception {
        // Given
        LibraryDto.Post post = getPost("", "address", "09:30", "05:30");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"name","","Name should not be null.");
    }

    @Test
    void postLibraryTest_Validation_nameTooLong() throws Exception {
        // Given
        LibraryDto.Post post = getPost("1234567891011121314151617181920", "address", "09:30", "05:30");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"name","1234567891011121314151617181920","Name should be at most 20 characters.");
    }

    @Test
    void postLibraryTest_Validation_nickNameNotBlank() throws Exception {
        // Given
        LibraryDto.Post post = getPost("name", "", "09:30", "05:30");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"address","","Address should not be null.");
    }

    @Test
    void postLibraryTest_Validation_addressTooLong() throws Exception {
        // Given
        LibraryDto.Post post = getPost("name", "012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "09:30", "05:30");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"address","012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890","Address should be at most 100 characters.");
        }

    @Test
    void postLibraryTest_Validation_invalidOpenTime() throws Exception {
        // Given
        LibraryDto.Post post = getPost("name", "address", "00:00:01", "05:30");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"openTime","00:00:01","Not valid openTime.(HH:MM)");
     }
    @Test
    void postLibraryTest_Validation_invalidCloseTime() throws Exception {
        // Given
        LibraryDto.Post post = getPost("name", "address", "09:30", "25:30");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"closeTime","25:30","Not valid closeTime.(HH:MM)");
    }
//    }
    private static LibraryDto.Post getPost(String name, String address, String openTime, String closeTime) {
        LibraryDto.Post post = LibraryDto.Post.builder()
                .name(name)
                .address(address)
                .openTime(openTime)
                .closeTime(closeTime)
                .build();
        ;
        return post;
    }
    private String getRequestBody() throws JsonProcessingException {
        LibraryDto.Post post = getPost("name", "address", "09:30", "05:30");
        return objectMapper.writeValueAsString(post);
    }


    private ResultActions getResult(String requestBody) throws Exception {
        ResultActions result;
        result = mockMvc.perform(
                post("/libraries")
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

