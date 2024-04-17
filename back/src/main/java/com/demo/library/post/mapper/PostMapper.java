package com.demo.library.post.mapper;

import com.demo.library.book.dto.BookDto;
import com.demo.library.book.service.BookService;
import com.demo.library.mapper.Mapper;
import com.demo.library.post.dto.PostDto;
import com.demo.library.post.entity.PostEntity;
import com.demo.library.user.entity.User;
import com.demo.library.utils.timeConverter.LocalDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {
    public final Mapper mapper;
    private final LocalDateTimeConverter localDateTimeConverter;
    private final BookService bookService;
    public PostEntity postToPostEntity(PostDto.Post post, User user ){
        PostEntity postEntity = mapper.map(post,PostEntity.class);
        postEntity.setBook(bookService.getBook(post.getBookId()));
        postEntity.setUser(user);
        return postEntity;
    }
    public PostEntity patchToPostEntity(PostDto.Patch patch){return mapper.map(patch, PostEntity.class);}
    public PostDto.Response postEntityToResponse(PostEntity postEntity) {
        PostDto.Response response = mapper.map(postEntity,PostDto.Response.class);

        response.setCreatedAt(localDateTimeConverter.convert(postEntity.getCreatedAt()));
        response.setUpdatedAt(localDateTimeConverter.convert(postEntity.getUpdatedAt()));

        response.setProfile(postEntity.getUser().getProfile());

        response.setBookImage(mapper.map(bookService.getBook(postEntity.getBook().getId()), BookDto.Image.class));
        response.setAuthorNickName(postEntity.getUser().getNickName());
        return response;
    }
    public PostDto.Listed postToList(PostEntity postEntity) {
        PostDto.Listed response = mapper.map(postEntity, PostDto.Listed.class);
        response.setAuthorNickName(postEntity.getUser().getNickName());
        response.setBookImage(mapper.map(bookService.getBook(postEntity.getBook().getId()), BookDto.Image.class));
        return response;
    }
    public List<PostDto.Listed> postsToList(List<PostEntity> posts) {
        return posts.stream().map(this::postToList).collect(Collectors.toList());
    }
}
