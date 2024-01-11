package com.demo.library.book.repository;


import com.demo.library.book.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface BookJPARepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findById(Long Id);
    Optional<BookEntity> findByTitle(String title);
    Page<BookEntity> findAllByTitleContaining(String keyword, Pageable pageable);
    Page<BookEntity> findAllByAuthor(String author, Pageable pageable);
    Page<BookEntity> findAllByPublisher(String publisher, Pageable pageable);
    Page<BookEntity> findTop10ByOrderBySearchCountDesc(Pageable pageable);

}
