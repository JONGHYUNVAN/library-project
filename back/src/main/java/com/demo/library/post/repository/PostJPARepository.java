package com.demo.library.post.repository;

import com.demo.library.post.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostJPARepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findById(Long Id);
    Optional<PostEntity> findByTitle(String title);
    @Query("SELECT new com.demo.library.post.entity.PostEntity(p.id, p.title, p.user, p.book, p.createdAt, p.updatedAt) FROM PostEntity p ORDER BY p.createdAt DESC")
    Page<PostEntity> findPostsByCreatedAtWithoutContent(Pageable pageable);
}
