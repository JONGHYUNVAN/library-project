package com.demo.library.book.controller;

import com.demo.library.book.dto.BookDto;
import com.demo.library.book.entity.BookEntity;
import com.demo.library.book.mapper.BookMapper;
import com.demo.library.book.service.BookService;
import com.demo.library.response.ResponseCreator;
import com.demo.library.response.dto.ListResponseDto;
import com.demo.library.response.dto.SingleResponseDto;
import com.demo.library.security.service.JWTAuthService;
import com.demo.library.user.service.UserService;
import com.demo.library.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Validated
public class BookController {
    private final static String BOOKS_DEFAULT_URL = "/books";
    private final BookService service;
    private final BookMapper mapper;
    private final UserService userService;
    private final JWTAuthService jwtAuthService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody BookDto.Post postDto) {

        BookEntity book = mapper.postToBook(postDto);
        service.createBook(book);

        URI location = UriCreator.createUri(BOOKS_DEFAULT_URL, book.getId());
        return ResponseCreator.created(location);
    }
    @PatchMapping
    public ResponseEntity<SingleResponseDto<BookDto.Response>>
                                update(@Valid @RequestBody BookDto.Patch patchDto) {

        service.isValidRequest(patchDto.getId());

        BookEntity requestBook = mapper.patchToBook(patchDto);
        BookEntity updatedBook = service.updateBook(requestBook);

        BookDto.Response responseDto = mapper.bookToResponse(updatedBook);
        return ResponseCreator.single(responseDto);
    }
    @GetMapping()
    public ResponseEntity<ListResponseDto<BookDto.Image>>get(Pageable pageable) {

        Page<BookEntity> bookPage = service.getBooksBySearchCount(pageable);
        List<BookEntity> bookList = bookPage.getContent();

        List<BookDto.Image> responseDto = mapper.booksToImages(bookList);
        return ResponseCreator.list(responseDto, bookPage.getTotalPages());
    }
    @GetMapping("/{book-id}")
    public ResponseEntity<SingleResponseDto<BookDto.Response>>
                                    get(@PathVariable("book-id") Long Id) {

        BookEntity book = service.getBook(Id);
        BookDto.Response responseDto = mapper.bookToResponse(book);

        Optional.ofNullable(jwtAuthService.getEmail())
                .ifPresent(email -> userService.updateUserGenreSearch(email, book.getGenre()));

        return ResponseCreator.single(responseDto);
    }
    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<ListResponseDto<BookDto.Image>>
                            getByKeyword(@PathVariable("keyword") String keyword, Pageable pageable) {
        Page<BookEntity> bookPage = service.getBooksByKeyword(keyword,pageable);
        List<BookEntity> bookList = bookPage.getContent();

        List<BookDto.Image> responseDto = mapper.booksToImages(bookList);
        return ResponseCreator.list(responseDto, bookPage.getTotalPages());
    }
    @GetMapping("/author/{author}")
    public ResponseEntity<ListResponseDto<BookDto.Response>>
                                 getByAuthor(@PathVariable("author") String author, Pageable pageable) {

        Page<BookEntity> bookPage = service.getBooksByAuthor(author,pageable);
        List<BookEntity> bookList = bookPage.getContent();

        List<BookDto.Response> responseDto = mapper.booksToResponses(bookList);
        return ResponseCreator.list(responseDto, bookPage.getTotalPages());
    }
    @GetMapping("/publisher/{publisher}")
    public ResponseEntity<ListResponseDto<BookDto.Response>>
                             getByPublisher(@PathVariable("publisher") String publisher, Pageable pageable) {

        Page<BookEntity> bookPage = service.getBooksByPublisher(publisher,pageable);
        List<BookEntity> bookList = bookPage.getContent();

        List<BookDto.Response> responseDto = mapper.booksToResponses(bookList);
        return ResponseCreator.list(responseDto, bookPage.getTotalPages());
    }
    @GetMapping("/logs/{id}")
    public ResponseEntity<ListResponseDto<String>> getBookLogs(@PathVariable("id") Long id) {
        BookEntity book = service.getBook(id);
        List<String> logs = service.convertLogs(book.getLogs());

        return ResponseCreator.list(logs,1);
    }
    @DeleteMapping("/{book-id}")
    public ResponseEntity<Void> delete(@PathVariable("book-id") Long Id) {

        service.deleteBook(Id);

        return ResponseCreator.deleted();
    }

}
