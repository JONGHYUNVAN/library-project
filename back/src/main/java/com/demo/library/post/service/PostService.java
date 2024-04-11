package com.demo.library.post.service;

import com.demo.library.exception.BusinessLogicException;
import com.demo.library.post.dto.PostDto;
import com.demo.library.post.entity.PostEntity;
import com.demo.library.post.repository.PostRepository;
import com.demo.library.user.entity.User;
import com.demo.library.utils.EntityUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.demo.library.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final EntityUpdater<PostEntity> entityUpdater;
    public PostEntity create(PostEntity postEntity){
        return postRepository.save(postEntity);
    }
    public PostEntity getPost(Long Id) {
        return verifyById(Id);
    }
    public PostEntity update(PostEntity savedEntity, PostEntity updatingEntity){
        entityUpdater.update(updatingEntity,savedEntity, PostEntity.class);

        return postRepository.save(savedEntity);
    }

    public PostEntity checkValidRequest(PostDto.Patch patchDto, User user){
        PostEntity postEntity = verifyById(patchDto.getId());
        if(!Objects.equals(postEntity.getId(), user.getId())) throw new BusinessLogicException(INVALID_POST_ID);
        else return postEntity;
    }
    public void deletePost(Long Id, User user){
        PostEntity postEntity = verifyById(Id);
        if(postEntity.getUser().equals(user)) postRepository.delete(postEntity);
        else throw new BusinessLogicException(NOT_ALLOWED);
    }
    // Inner Methods
    public PostEntity verifyById(Long Id) {
        return postRepository.findById(Id)
                .orElseThrow(() -> new BusinessLogicException(POST_NOT_FOUND));
    }

}
