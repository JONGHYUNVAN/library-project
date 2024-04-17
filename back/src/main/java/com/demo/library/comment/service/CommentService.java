package com.demo.library.comment.service;

import com.demo.library.comment.entity.Comment;
import com.demo.library.exception.BusinessLogicException;
import com.demo.library.comment.dto.CommentDto;
import com.demo.library.comment.repository.CommentRepository;
import com.demo.library.user.entity.User;
import com.demo.library.utils.EntityUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.demo.library.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EntityUpdater<Comment> entityUpdater;
    public Comment create(Comment comment){
        return commentRepository.save(comment);
    }
    public Comment getComment(Long Id) {
        return verifyById(Id);
    }
    public Comment update(Comment savedEntity, Comment updatingEntity){
        entityUpdater.update(updatingEntity,savedEntity, Comment.class);

        return commentRepository.save(savedEntity);
    }

    public Comment checkValidRequest(CommentDto.Patch patchDto, User user){
        Comment Comment = verifyById(patchDto.getId());
        if(!Objects.equals(Comment.getId(), user.getId())) throw new BusinessLogicException(INVALID_POST_ID);
        else return Comment;
    }
    public void deleteComment(Long Id, User user){
        Comment Comment = verifyById(Id);
        if(Comment.getUser().equals(user)) commentRepository.delete(Comment);
        else throw new BusinessLogicException(NOT_ALLOWED);
    }

    public Comment verifyById(Long Id) {
        return commentRepository.findById(Id)
                .orElseThrow(() -> new BusinessLogicException(POST_NOT_FOUND));
    }

    public boolean checkIsAuthor(Comment Comment, User user) {
        return Comment.getUser().equals(user);
    }

    public Page<Comment> getCommentsByPostId(Long postId, Pageable pageable) {
       return commentRepository.findAllByPostId(postId,pageable);
    }
}
