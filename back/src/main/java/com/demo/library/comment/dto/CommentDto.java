package com.demo.library.comment.dto;

import com.demo.library.book.dto.BookDto;
import com.demo.library.utils.customValidator.notBlank.NotBlankValidation;
import com.demo.library.utils.customValidator.size.SizeValidation;
import lombok.*;


public class CommentDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private Long postId;
        private String content;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private Long id;
        private String content;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String authorNickName;
        private String content;
        private String createdAt;
        private String updatedAt;
    }

}
