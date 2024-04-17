package com.demo.library.comment.repository;
import com.demo.library.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentJPARepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long Id);
    Page<Comment> findAllByPostId(Long postId, Pageable pageable);
}
