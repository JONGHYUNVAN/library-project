package com.demo.library.post.dto;

import com.demo.library.book.dto.BookDto;
import com.demo.library.utils.customValidator.notBlank.NotBlankValidation;
import com.demo.library.utils.customValidator.size.SizeValidation;
import lombok.*;


public class PostDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlankValidation(fieldName = "Name")
        @SizeValidation(max=30, fieldName = "Name")
        private String title;
        private String content;
        private Long bookId;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private Long id;
        private String title;
        private String content;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String createdAt;
        private String updatedAt;
        private String authorNickName;
        private Long views;
        private BookDto.Image bookImage;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Listed {
        private Long id;
        private String title;
        private String authorNickName;
        private Long views;
        private BookDto.Image bookImage;
    }

}
