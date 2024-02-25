package com.demo.library.book.mapper;


import com.demo.library.utils.timeConverter.LocalDateTimeConverter;
import com.demo.library.book.dto.BookDto;
import com.demo.library.book.entity.BookEntity;
import com.demo.library.library.entity.Library;
import com.demo.library.library.service.LibraryService;
import com.demo.library.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final LibraryService libraryService;
    private final Mapper mapper;
    private final LocalDateTimeConverter localDateTimeConverter;

    public BookEntity postToBook(BookDto.Post post) {
        Library library = libraryService.verifyById(post.getLibraryId());
        BookEntity book = mapper.map(post,BookEntity.class);
        book.setLibrary(library);
        return book;
    }
    public BookEntity patchToBook(BookDto.Patch patch)
    {
        return mapper.map(patch,BookEntity.class);
    }
    public BookDto.Response bookToResponse(BookEntity book) {
        BookDto.Response response = mapper.map(book,BookDto.Response.class);
        response.setLibraryId(book.getLibrary().getId());
        response.setLibraryName(book.getLibrary().getName());
        response.setGenreName(book.getGenre().getName());
        response.setCreatedAt(localDateTimeConverter.convert(book.getCreatedAt()));
        response.setUpdatedAt(localDateTimeConverter.convert(book.getUpdatedAt()));
        return response;
    }
    public BookDto.Image bookToImage(BookEntity book) {
        BookDto.Image image = mapper.map(book,BookDto.Image.class);
        return image;
    }
    public List<BookDto.Response> booksToResponses(List<BookEntity> books) {
        return books.stream().map(this::bookToResponse).collect(Collectors.toList());
    }
    public List<BookDto.Image> booksToImages(List<BookEntity> books) {
        return books.stream().map(this::bookToImage).collect(Collectors.toList());
    }


}
