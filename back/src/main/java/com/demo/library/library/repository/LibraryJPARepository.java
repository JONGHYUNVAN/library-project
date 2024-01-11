package com.demo.library.library.repository;

import com.demo.library.library.entity.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryJPARepository extends JpaRepository<Library, Long> {
    Optional<Library> findById(Long Id);
    Optional<Library> findByName(String name);
    Page<Library> findAllByNameContaining(String keyword, Pageable pageable);
}
