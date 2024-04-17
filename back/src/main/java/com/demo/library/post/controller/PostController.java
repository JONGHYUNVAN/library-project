package com.demo.library.post.controller;

import com.demo.library.book.dto.BookDto;
import com.demo.library.book.entity.BookEntity;
import com.demo.library.book.mapper.BookMapper;
import com.demo.library.book.service.BookService;
import com.demo.library.exception.BusinessLogicException;
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
import java.util.Optional;

import static com.demo.library.exception.ExceptionCode.NOT_ALLOWED;

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
        postEntity.setViews(0L);
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
    public ResponseEntity<ListResponseDto<PostDto.Listed>>
                                get(Pageable pageable) {
        Page<PostEntity> postPage = service.getPostsByCreatedAt(pageable);
        List<PostEntity> postList = postPage.getContent();

        List<PostDto.Listed> listedPosts = mapper.postsToList(postList);

        return ResponseCreator.list(listedPosts,postPage.getTotalPages());
    }
    @GetMapping("/book-posts/{book-id}")
    public ResponseEntity<ListResponseDto<PostDto.Listed>>
                                get(Pageable pageable,@PathVariable("book-id") Long Id) {
        Page<PostEntity> postPage = service.getPostsByBookId(pageable,Id);
        List<PostEntity> postList = postPage.getContent();

        List<PostDto.Listed> listedPosts = mapper.postsToList(postList);

        return ResponseCreator.list(listedPosts,postPage.getTotalPages());
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<SingleResponseDto<PostDto.Response>>
                                get(@PathVariable("post-id") Long Id) {

        PostEntity postEntity = service.getPost(Id);
        PostDto.Response responseDto = mapper.postEntityToResponse(postEntity);

        Boolean isAuthor = Optional.ofNullable(jwtAuthService.getEmail())
                .map(email -> service.checkIsAuthor(postEntity, userService.findByEmail(email)))
                .orElse(false);

        responseDto.setIsAuthor(isAuthor);
        return ResponseCreator.single(responseDto);
    }
    @DeleteMapping("/{post-id}")
    public ResponseEntity<Void> delete(@PathVariable("post-id") Long Id) {

        PostEntity postEntity = service.getPost(Id);
        if(service.checkIsAuthor(postEntity,userService.findByEmail(jwtAuthService.getEmail())))
            service.deletePost(Id,userService.findByEmail(jwtAuthService.getEmail()));
        else throw new BusinessLogicException(NOT_ALLOWED);

        return ResponseCreator.deleted();
    }
}
