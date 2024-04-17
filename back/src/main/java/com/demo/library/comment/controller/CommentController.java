package com.demo.library.comment.controller;

import com.demo.library.book.service.BookService;
import com.demo.library.exception.BusinessLogicException;
import com.demo.library.comment.dto.CommentDto;
import com.demo.library.comment.entity.Comment;
import com.demo.library.comment.mapper.CommentMapper;
import com.demo.library.comment.service.CommentService;
import com.demo.library.response.ResponseCreator;
import com.demo.library.response.dto.ListResponseDto;
import com.demo.library.response.dto.SingleResponseDto;
import com.demo.library.security.service.JWTAuthService;
import com.demo.library.user.service.UserService;
import com.demo.library.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.demo.library.exception.ExceptionCode.NOT_ALLOWED;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final static String POST_DEFAULT_URL = "/comments";
    private final CommentService service;
    private final CommentMapper mapper;
    private final JWTAuthService jwtAuthService;
    private final UserService userService;
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CommentDto.Post postDto) {
        Comment comment = mapper.postToComment(postDto,userService.findByEmail(jwtAuthService.getEmail()));

        service.create(comment);

        URI location = UriCreator.createUri(POST_DEFAULT_URL, comment.getId());
        return ResponseCreator.created(location);
    }
    @PatchMapping
    public ResponseEntity<SingleResponseDto<CommentDto.Response>>
                                 update(@Valid @RequestBody CommentDto.Patch patchDto) {
        Comment savedEntity = service.checkValidRequest(patchDto,userService.findByEmail(jwtAuthService.getEmail()));
        Comment updatingComment = mapper.patchToComment(patchDto);
        Comment updatedComment = service.update(savedEntity,updatingComment);
        CommentDto.Response responseDto = mapper.commentToResponse(updatedComment);
        return ResponseCreator.single(responseDto);
    }
    @GetMapping("/post-comments/{postId}")
    public ResponseEntity<ListResponseDto<CommentDto.Response>> get(@PathVariable Long postId, Pageable pageable) {
        Page<Comment> commentPage = service.getCommentsByPostId(postId, pageable);
        List<Comment> commentList = commentPage.getContent();

        List<CommentDto.Response> listedComments = mapper.commentsToList(commentList);

        return ResponseCreator.list(listedComments,commentPage.getTotalPages());
    }

    @GetMapping("/{comment-id}")
    public ResponseEntity<SingleResponseDto<CommentDto.Response>>
                                get(@PathVariable("comment-id") Long Id) {

        Comment comment = service.getComment(Id);
        CommentDto.Response responseDto = mapper.commentToResponse(comment);

        return ResponseCreator.single(responseDto);
    }
    @DeleteMapping("/{comment-id}")
    public ResponseEntity<Void> delete(@PathVariable("comment-id") Long Id) {

        Comment comment = service.getComment(Id);
        if(service.checkIsAuthor(comment,userService.findByEmail(jwtAuthService.getEmail())))
            service.deleteComment(Id,userService.findByEmail(jwtAuthService.getEmail()));
        else throw new BusinessLogicException(NOT_ALLOWED);

        return ResponseCreator.deleted();
    }
}
