package com.demo.library.library.mapper;


import com.demo.library.book.dto.BookDto;
import com.demo.library.book.entity.BookEntity;
import com.demo.library.library.dto.LibraryDto;
import com.demo.library.library.entity.Library;
import com.demo.library.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LibraryMapper {
    private final Mapper mapper;

    public Library postToLibrary(LibraryDto.Post post) {
        return mapper.map(post,Library.class);
    }
    public Library patchToLibrary(LibraryDto.Patch patch) {
        return mapper.map(patch,Library.class);
    }
    public LibraryDto.Response libraryToResponse(Library library) {
        return mapper.map(library,LibraryDto.Response.class);
    }
    public List<LibraryDto.Response> librariesToResponses(List<Library> libraries) {
        return libraries.stream().map(this::libraryToResponse).collect(Collectors.toList());
    }

}
