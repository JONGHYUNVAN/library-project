package com.demo.library.post.repository;

import com.demo.library.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostJPARepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findById(Long Id);
    Optional<PostEntity> findByTitle(String title);
}
