package com.demo.library.user.controller;

import com.demo.library.user.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postUserTest() throws Exception {
        // Given
        String requestBody = getRequestBody("name","nickName","010-1234-5678","MALE");

        // When
        ResultActions result = getResult(requestBody);

        // Then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/users")));
    }




    @Test
    void postUserTest_Validation_nameNotBlank() throws Exception {
        // Given
        UserDto.Post post = getPost("", "nickName1", "010-1234-5678", "MALE");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"name","","Name should not be null.");
    }


    @Test
    void postUserTest_Validation_nameTooLong() throws Exception {
        // Given
        UserDto.Post post = getPost("1234567891011121314151617181920", "nickName", "010-1234-5678", "MALE");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"name","1234567891011121314151617181920","Name should be at most 20 characters.");
    }

    @Test
    void postUserTest_Validation_nickNameNotBlank() throws Exception {
        // Given
        UserDto.Post post = getPost("user1", "", "010-1234-5678", "MALE");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"nickName","","NickName should not be null.");
    }

    @Test
    void postUserTest_Validation_nickNameTooLong() throws Exception {
        // Given
        UserDto.Post post = getPost("user1", "1234567891011121314151617181920", "010-1234-5678", "MALE");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"nickName","1234567891011121314151617181920","NickName should be at most 20 characters.");
        }

    @Test
    void postUserTest_Validation_invalidPhoneNumber() throws Exception {
        // Given
        UserDto.Post post = getPost("user1", "nickName1", "12345678", "MALE");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"phoneNumber","12345678","Invalid phone number.");
     }

    @Test
    void postUserTest_Validation_invalidGender() throws Exception {
        // Given
        UserDto.Post post = getPost("user1", "nickName1", "010-1234-5678", "Fling Spaghetti");
        String requestBody = objectMapper.writeValueAsString(post);

        // When
        ResultActions result = getResult(requestBody);

        // Then
        assertWith(result,"gender","Fling Spaghetti","Gender should be MALE or FEMALE.");
    }
    private static UserDto.Post getPost(String name, String nickName, String phoneNumber, String gender) {
        UserDto.Post post =UserDto.Post.builder()
                .name(name)
                .nickName(nickName)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .build();
        ;
        return post;
    }
    private String getRequestBody(String name, String nickName, String phoneNumber, String gender) throws JsonProcessingException {
        UserDto.Post post = getPost(name, nickName, phoneNumber, gender);
        String requestBody = objectMapper.writeValueAsString(post);
        return requestBody;
    }


    private ResultActions getResult(String requestBody) throws Exception {
        ResultActions result = mockMvc.perform(
                post("/users")
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

