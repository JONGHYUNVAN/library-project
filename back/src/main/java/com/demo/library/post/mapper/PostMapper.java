package com.demo.library.post.mapper;

import com.demo.library.book.dto.BookDto;
import com.demo.library.book.entity.BookEntity;
import com.demo.library.mapper.Mapper;
import com.demo.library.post.dto.PostDto;
import com.demo.library.post.entity.PostEntity;
import com.demo.library.user.entity.User;
import com.demo.library.utils.timeConverter.LocalDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
    public final Mapper mapper;
    private final LocalDateTimeConverter localDateTimeConverter;

    public PostEntity postToPostEntity(PostDto.Post post, User user ){
        PostEntity postEntity = mapper.map(post,PostEntity.class);
        postEntity.setUser(user);
        return postEntity;
    }
    public PostEntity patchToPostEntity(PostDto.Patch patch){return mapper.map(patch, PostEntity.class);}
    public PostDto.Response postEntityToResponse(PostEntity postEntity) {
        PostDto.Response response = mapper.map(postEntity,PostDto.Response.class);
        response.setCreatedAt(localDateTimeConverter.convert(postEntity.getCreatedAt()));
        response.setUpdatedAt(localDateTimeConverter.convert(postEntity.getUpdatedAt()));
        response.setAuthorNickName(postEntity.getUser().getNickName());
        return response;
    }
}
