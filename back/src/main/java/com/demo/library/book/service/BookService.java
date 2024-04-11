package com.demo.library.book.service;

import com.demo.library.book.repository.BookJPARepository;
import com.demo.library.exception.BusinessLogicException;
import com.demo.library.utils.EntityUpdater;
import com.demo.library.utils.timeConverter.LocalDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.demo.library.book.entity.BookEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.demo.library.book.entity.BookEntity.Status.AVAILABLE;
import static com.demo.library.book.entity.BookEntity.Status.ON_LOAN;
import static com.demo.library.exception.ExceptionCode.*;


@Service
@RequiredArgsConstructor
public class BookService {
    private final BookJPARepository bookRepository;
    private final EntityUpdater<BookEntity> entityUpdater;
    private final LocalDateTimeConverter localDateTimeConverter;


    public void createBook(BookEntity book) {
        verifyByTitle(book);
        book.setStatus(AVAILABLE);

        bookRepository.save(book);
    }

    public BookEntity updateBook(BookEntity book) {
        Long bookId = book.getId();
        BookEntity verifiedBook = verifyById(bookId);

        entityUpdater.update(book, verifiedBook, BookEntity.class);

        return bookRepository.save(verifiedBook);
    }

    public BookEntity getBook(long Id) {
        BookEntity verifiedBook = verifyById(Id);

        increaseSearchCount(verifiedBook);

        return bookRepository.save(verifiedBook);
    }

    private static void increaseSearchCount(BookEntity verifiedBook) {
        verifiedBook.setSearchCount(verifiedBook.getSearchCount()+1);
    }
    public Page<BookEntity> getBooksBySearchCount(Pageable pageable) {
        PageRequest of = PageRequest.of(pageable.getPageNumber() ,
                pageable.getPageSize(),
                pageable.getSort()
                        .and(Sort.by("searchCount").descending())
        );

        return bookRepository.findTop10ByOrderBySearchCountDesc(of);
    }

    public Page<BookEntity> getBooksByKeyword(String keyword, Pageable pageable) {
        PageRequest of = PageRequest.of(pageable.getPageNumber() ,
                                        pageable.getPageSize(),
                                        pageable.getSort()
                                                .and(Sort.by("searchCount").descending())
                                        );

        return bookRepository.findAllByTitleContaining(keyword,of);
    }
    public Page<BookEntity> getBooksByAuthor(String author, Pageable pageable) {
        PageRequest of = PageRequest.of(pageable.getPageNumber() ,
                                        pageable.getPageSize(),
                                        pageable.getSort()
                                                .and(Sort.by("searchCount").descending())
        );

        return bookRepository.findAllByAuthor(author,of);
    }
    public Page<BookEntity> getBooksByPublisher(String publisher, Pageable pageable) {
        PageRequest of = PageRequest.of(pageable.getPageNumber() ,
                pageable.getPageSize(),
                pageable.getSort()
                        .and(Sort.by("searchCount").descending())
        );

        return bookRepository.findAllByPublisher(publisher,of);
    }

    public void deleteBook(Long Id) {
        BookEntity verifiedBookEntity = verifyById(Id);
        ifOnLoan(verifiedBookEntity);
        bookRepository.delete(verifiedBookEntity);
    }

    // Inner Methods
    public void isValidRequest(Long Id){
        Optional.ofNullable(Id)
                .orElseThrow(() -> new BusinessLogicException(BOOK_NOT_FOUND));
    }

    public void loanBook(BookEntity BookEntity){
        BookEntity.setStatus(ON_LOAN);
        bookRepository.save(BookEntity);
    }

    public void ifOnLoan(BookEntity BookEntity) {
        if (BookEntity.getStatus().equals(ON_LOAN))
            throw new BusinessLogicException(BOOK_IS_ON_LOAN);
    }

    private void verifyByTitle(BookEntity BookEntity) {
        String title = BookEntity.getTitle();
        Optional<BookEntity> optionalBook = bookRepository.findByTitle(title);
        optionalBook.ifPresent(s -> {
            throw new BusinessLogicException(BOOK_ALREADY_EXISTS);
        });
    }

    public BookEntity verifyById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessLogicException(INVALID_BOOK_ID));
    }

    public List<String> convertLogs (List<String>logs){

        return logs.stream()
                .map(this::convertLocalDateTime)
                .collect(Collectors.toList());
    }

    private String convertLocalDateTime(String log) {
        String[] parts = log.split(" ");
        String from =  localDateTimeConverter.convert(LocalDateTime.parse(parts[2]));
        String to = localDateTimeConverter.convert(LocalDateTime.parse(parts[3]));

        return parts[0] + " " + parts[1] + " " + from + " " + to;
    }
}
