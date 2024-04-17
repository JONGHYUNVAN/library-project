package com.demo.library.comment.mapper;

import com.demo.library.book.dto.BookDto;
import com.demo.library.book.service.BookService;
import com.demo.library.comment.dto.CommentDto;
import com.demo.library.comment.entity.Comment;
import com.demo.library.mapper.Mapper;
import com.demo.library.post.service.PostService;
import com.demo.library.user.entity.User;
import com.demo.library.utils.timeConverter.LocalDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    public final Mapper mapper;
    private final LocalDateTimeConverter localDateTimeConverter;
    private final PostService postService;
    public Comment postToComment(CommentDto.Post postDto, User user ){
        Comment comment = mapper.map(postDto, Comment.class);
        comment.setUser(user);
        comment.setPost(postService.verifyById(postDto.getPostId()));
        return comment;
    }
    public Comment patchToComment(CommentDto.Patch patch){return mapper.map(patch, Comment.class);}
    public CommentDto.Response commentToResponse(Comment comment) {
        CommentDto.Response response = mapper.map(comment, CommentDto.Response.class);

        response.setAuthorNickName(comment.getUser().getNickName());

        response.setCreatedAt(localDateTimeConverter.convert(comment.getCreatedAt()));
        response.setUpdatedAt(localDateTimeConverter.convert(comment.getUpdatedAt()));


        return response;
    }
    public List<CommentDto.Response> commentsToList(List<Comment> Comments) {
        return Comments.stream().map(this::commentToResponse).collect(Collectors.toList());
    }
}
