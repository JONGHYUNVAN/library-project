package com.demo.library.user.controller;

import com.demo.library.user.dto.UserDto;
import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class PatchTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void  setup() {
        User existingUser = User.builder()
                .id(1L)
                .name("existingName")
                .password("safePassword")
                .email("address@email.com")
                .nickName("existingNickName")
                .phoneNumber("010-1234-5678")
                .gender(User.Gender.MALE)
                .status(User.Status.ACTIVE)

                .build();

        userRepository.save(existingUser);
    }
    @Test
    void patchUserTest() throws Exception {
        // Given
        String requestBody = getRequestBody(1L,"newName","newNickName","010-5678-1234","FEMALE");

        // When
        ResultActions result = getResultAction(requestBody);

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("newName"))
                .andExpect(jsonPath("$.data.nickName").value("newNickName"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-5678-1234"))
                .andExpect(jsonPath("$.data.gender").value("FEMALE"));
    }

    @Test
    public void patchUserTest_Validation_nameNotBlank() throws Exception {
        UserDto.Patch patch = getPatch(1L, "", "newNickName", "010-5678-1234", "FEMALE");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = getResultAction(requestBody);

        //Then
        getExpect(result,"name","","Name should not be null.");               ;

    }

    @Test
    void patchUserTest_Validation_nameTooLong() throws Exception {
        // Given
        UserDto.Patch patch = getPatch(null, "1234567891011121314151617181920", "nickName", "010-1234-5678", "MALE");

        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = getResultAction(requestBody);

        // Then
        getExpect(result, "name", "1234567891011121314151617181920", "Name should be at most 20 characters.");
    }

    @Test
    public void patchUserTest_Validation_nickNameNotBlank() throws Exception {
        UserDto.Patch patch = getPatch(1L, "name", "", "010-5678-1234", "FEMALE");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = getResultAction(requestBody);

        //Then
        getExpect(result, "nickName", "", "NickName should not be null.");
    }
    @Test
    void postUserTest_Validation_nickNameTooLong() throws Exception {
        // Given
        UserDto.Patch patch = getPatch(null, "user1", "1234567891011121314151617181920", "010-1234-5678", "MALE");
        String requestBody = objectMapper.writeValueAsString(patch);
        // When
        ResultActions result = getResultAction(requestBody);

        // Then
        getExpect(result, "nickName", "1234567891011121314151617181920", "NickName should be at most 20 characters.");
    }
    @Test
    public void patchUserTest_Validation_invalidPhoneNumber() throws Exception {
        UserDto.Patch patch = getPatch(1L, "name", "newNickName", "010-5678-12345", "FEMALE");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = getResultAction(requestBody);

        //Then
        getExpect(result, "phoneNumber", "010-5678-12345", "Invalid phone number.");
    }
    @Test
    public void patchUserTest_Validation_invalidGender() throws Exception {
        UserDto.Patch patch = getPatch(1L, "name", "newNickName", "010-5678-1234", "Apache Helicopter");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = getResultAction(requestBody);

        //Then
        getExpect(result, "gender", "Apache Helicopter", "Gender should be MALE or FEMALE.");
    }
    private String getRequestBody(Long id, String name, String nickName, String phoneNumber, String gender) throws JsonProcessingException {

        UserDto.Patch patch = getPatch(id, name, nickName, phoneNumber, gender);
        String requestBody = objectMapper.writeValueAsString(patch);
        return requestBody;
    }
    private ResultActions getResultAction(String requestBody) throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        return result;
    }
    private static UserDto.Patch getPatch(Long id, String name, String nickName, String phoneNumber, String gender) {
        UserDto.Patch patch = UserDto.Patch.builder()
                .id(id)
                .name(name)

                .nickName(nickName)
                .phoneNumber(phoneNumber)
                .gender(gender)
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
