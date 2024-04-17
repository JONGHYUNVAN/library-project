package com.demo.library.post.repository;

import com.demo.library.post.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostJPARepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findById(Long Id);
    Optional<PostEntity> findByTitle(String title);
    @Query("SELECT new com.demo.library.post.entity.PostEntity(p.id, p.title, p.user, p.book, p.createdAt, p.updatedAt,p.views) FROM PostEntity p ORDER BY p.createdAt DESC")
    Page<PostEntity> findPostsByCreatedAtWithoutContent(Pageable pageable);
    @Query("SELECT new com.demo.library.post.entity.PostEntity(p.id, p.title, p.user, p.book, p.createdAt, p.updatedAt,p.views) FROM PostEntity p WHERE p.book.id = :bookId ORDER BY p.createdAt DESC")
    Page<PostEntity> findPostsByBookIdAndOrderByCreatedAtDesc(Pageable pageable, @Param("bookId") Long bookId);
}
