package com.demo.library.post.controller;

import com.demo.library.book.dto.BookDto;
import com.demo.library.book.entity.BookEntity;
import com.demo.library.book.mapper.BookMapper;
import com.demo.library.book.service.BookService;
import com.demo.library.post.dto.PostDto;
import com.demo.library.post.entity.PostEntity;
import com.demo.library.post.mapper.PostMapper;
import com.demo.library.post.service.PostService;
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

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final static String POST_DEFAULT_URL = "/posts";
    private final PostService service;
    private final PostMapper mapper;
    private final JWTAuthService jwtAuthService;
    private final UserService userService;
    private final BookService bookService;
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody PostDto.Post post) {
        PostEntity postEntity = mapper.postToPostEntity(post,userService.findByEmail(jwtAuthService.getEmail()));
        postEntity.setBook(bookService.getBook(post.getBookId()));

        service.create(postEntity);

        URI location = UriCreator.createUri(POST_DEFAULT_URL, postEntity.getId());
        return ResponseCreator.created(location);
    }
    @PatchMapping
    public ResponseEntity<SingleResponseDto<PostDto.Response>>
                                 update(@Valid @RequestBody PostDto.Patch patchDto) {
        PostEntity savedEntity = service.checkValidRequest(patchDto,userService.findByEmail(jwtAuthService.getEmail()));
        PostEntity updatingPost = mapper.patchToPostEntity(patchDto);
        PostEntity updatedPost = service.update(savedEntity,updatingPost);
        PostDto.Response responseDto = mapper.postEntityToResponse(updatedPost);
        return ResponseCreator.single(responseDto);
    }
    @GetMapping()
    public ResponseEntity<ListResponseDto<PostDto.List>>
                                get(Pageable pageable) {
        Page<PostEntity> postPage = service.getPostsByCreatedAt(pageable);
        List<PostEntity> postList = postPage.getContent();

        List<PostDto.List> responseDto = mapper.postsToList(postList);
        return ResponseCreator.list(responseDto);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<SingleResponseDto<PostDto.Response>>
                                get(@PathVariable("post-id") Long Id) {

        PostEntity postEntity = service.getPost(Id);
        PostDto.Response responseDto = mapper.postEntityToResponse(postEntity);
        return ResponseCreator.single(responseDto);
    }
    @DeleteMapping("/{post-id}")
    public ResponseEntity<Void> delete(@PathVariable("post-id") Long Id) {

        service.deletePost(Id,userService.findByEmail(jwtAuthService.getEmail()));

        return ResponseCreator.deleted();
    }
}
