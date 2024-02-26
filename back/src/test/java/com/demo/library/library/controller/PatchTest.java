package com.demo.library.library.controller;


import com.demo.library.library.dto.LibraryDto;
import com.demo.library.library.entity.Library;
import com.demo.library.library.repository.LibraryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;

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
    @Mock
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
    }
    @Test
    void patchLibraryTest() throws Exception {
        // Given
        String requestBody = getRequestBody(1L,"newName","newAddress","09:35","17:25");

        // When
        ResultActions result = getResultAction(requestBody);


        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("newName"))
                .andExpect(jsonPath("$.data.address").value("newAddress"))
                .andExpect(jsonPath("$.data.openTime").value("09:35"))
                .andExpect(jsonPath("$.data.closeTime").value("17:25"));
    }

    @Test
    public void patchLibraryTest_Validation_nameNotBlank() throws Exception {
        LibraryDto.Patch patch = getPatch(1L,"","newAddress","09:35","17:25");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        //Then
        getExpect(result,"name","","Name should not be null.");               ;

    }

    @Test
    void patchLibraryTest_Validation_nameTooLong() throws Exception {
        // Given
        LibraryDto.Patch patch = getPatch(1L, "1234567891011121314151617181920", "newAddress", "09:35","17:25");

        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        getExpect(result, "name", "1234567891011121314151617181920", "Name should be at most 20 characters.");
    }

    @Test
    public void patchLibraryTest_Validation_addressNotBlank() throws Exception {
        LibraryDto.Patch patch = getPatch(1L, "name", "", "09:35","17:25");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        //Then
        getExpect(result, "address", "", "Address should not be null.");
    }
    @Test
    void postLibraryTest_Validation_addressTooLong() throws Exception {
        // Given
        LibraryDto.Patch patch = getPatch(null, "library1", "012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "09:35","17:25");

        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        getExpect(result, "address", "012345678910123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "Address should be at most 100 characters.");
    }
    @Test
    public void patchLibraryTest_Validation_invalidOpenTime() throws Exception {
        LibraryDto.Patch patch = getPatch(1L, "name", "address", "00:00:01", "05:30");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        //Then
        getExpect(result, "openTime","00:00:01","Not valid openTime.(HH:MM)");
    }
    @Test
    public void patchLibraryTest_Validation_invalidGender() throws Exception {
        LibraryDto.Patch patch = getPatch(1L, "name", "address", "09:30", "25:30");
        String requestBody = objectMapper.writeValueAsString(patch);

        // When
        ResultActions result = mockMvc.perform(
                patch("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        //Then
        getExpect(result, "closeTime","25:30","Not valid closeTime.(HH:MM)");
    }
    private String getRequestBody(Long id, String name, String address, String openTime, String closeTime) throws JsonProcessingException {

        LibraryDto.Patch patch = getPatch(id, name, address, openTime, closeTime);
        String requestBody = objectMapper.writeValueAsString(patch);
        return requestBody;
    }

    private static LibraryDto.Patch getPatch(Long id, String name, String address, String openTime, String closeTime) {
        LibraryDto.Patch patch = LibraryDto.Patch.builder()
                .id(id)
                .name(name)
                .address(address)
                .openTime(openTime)
                .closeTime(closeTime)

                .build();
        return patch;
    }
    private static ResultActions getExpect(ResultActions result, String field, String rejectValue, String reason) throws Exception {
        return result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value(field))
                .andExpect(jsonPath("$.fieldErrors[0].rejectValue").value(rejectValue))
                .andExpect(jsonPath("$.fieldErrors[0].reason").value(reason));
    }
    private ResultActions getResultAction(String requestBody) throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        return result;
    }
}
