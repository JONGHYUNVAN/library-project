package com.demo.library.library.dto;


import com.demo.library.utils.customValidator.notBlank.NotBlankValidation;
import com.demo.library.utils.customValidator.pattern.notEmpty.NotEmptyValidation;
import com.demo.library.utils.customValidator.size.SizeValidation;
import lombok.*;


import javax.validation.constraints.*;

public class LibraryDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlankValidation(fieldName = "Name")
        @SizeValidation(max=20, fieldName = "Name")
        private String name;
        @NotBlankValidation(fieldName = "Address")
        @SizeValidation(max=100, fieldName = "Address")
        private String address;
        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Not valid openTime.(HH:MM)")
        private String openTime;
        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Not valid closeTime.(HH:MM)")
        private String closeTime;

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private Long id;
        @NotEmptyValidation(fieldName = "Name")
        @SizeValidation(max=20, fieldName = "Name")
        private String name;
        @NotEmptyValidation(fieldName = "Address")
        @SizeValidation(max=100, fieldName = "Address")
        private String address;
        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Not valid openTime.(HH:MM)")
        private String openTime;
        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Not valid closeTime.(HH:MM)")
        private String closeTime;

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String address;
        private String openTime;
        private String closeTime;

    }

}
