package com.demo.library.book.dto;


import com.demo.library.utils.customValidator.notBlank.NotBlankValidation;
import com.demo.library.utils.customValidator.pattern.notEmpty.NotEmptyValidation;
import com.demo.library.utils.customValidator.size.SizeValidation;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import javax.validation.constraints.*;

public class BookDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlankValidation(fieldName = "Title")
        @SizeValidation(max=20,fieldName = "Title")
        private String title;
        @NotBlankValidation(fieldName = "Publisher")
        @SizeValidation(max=20,fieldName = "Publisher")
        private String publisher;
        @NotBlankValidation(fieldName = "Author")
        @SizeValidation(max=20,fieldName = "Author")
        private String author;
        @NotNull(message = "Must contain libraryId.")
        private Long libraryId;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private Long id;
        @NotEmptyValidation(fieldName = "Title")
        @SizeValidation(max = 20, fieldName = "Title")
        private String title;
        @NotEmptyValidation(fieldName = "Publisher")
        @SizeValidation(max = 20, fieldName = "Publisher")
        private String publisher;
        @NotEmptyValidation(fieldName = "Author")
        @SizeValidation(max = 20, fieldName = "Author")
        private String author;


    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonRootName(value="book")
    public static class Response {
        private Long id;
        private String title;
        private String publisher;
        private String author;
        private String status;
        private String createdAt;
        private String updatedAt;
        private Long libraryId;
        private String libraryName;

    }

}
