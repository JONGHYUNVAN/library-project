package com.demo.library.library.controller;


import com.demo.library.library.dto.LibraryDto;
import com.demo.library.library.entity.Library;
import com.demo.library.library.mapper.LibraryMapper;
import com.demo.library.library.service.LibraryService;
import com.demo.library.response.ResponseCreator;
import com.demo.library.response.dto.ListResponseDto;
import com.demo.library.response.dto.SingleResponseDto;
import com.demo.library.utils.UriCreator;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryController {
    private final static String LIBRARIES_DEFAULT_URL = "/libraries";
    private final LibraryService service;
    private final LibraryMapper mapper;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody LibraryDto.Post postDto) {

        Library library = mapper.postToLibrary(postDto);
        service.create(library);

        URI location = UriCreator.createUri(LIBRARIES_DEFAULT_URL, library.getId());
        return ResponseCreator.created(location);
    }

    @PatchMapping
    public ResponseEntity<SingleResponseDto<LibraryDto.Response>>
                               update(@Valid @RequestBody LibraryDto.Patch patchDto) {

        service.verifyId(patchDto.getId());

        Library requestLibrary = mapper.patchToLibrary(patchDto);
        Library updatedLibrary = service.update(requestLibrary);

        LibraryDto.Response responseDto = mapper.libraryToResponse(updatedLibrary);
        return ResponseCreator.single(responseDto);
    }

    @GetMapping("/{library-id}")
    public ResponseEntity<SingleResponseDto<LibraryDto.Response>>
                               get(@PathVariable("library-id") Long Id) {

        Library library = service.get(Id);
        LibraryDto.Response responseDto = mapper.libraryToResponse(library);

        return ResponseCreator.single(responseDto);
    }
    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<ListResponseDto<LibraryDto.Response>>
                               get(@PathVariable("keyword") String keyword, Pageable pageable) {

        Page<Library> libraryPage = service.getLibrariesByKeyword(keyword,pageable);
        List<Library> libraryList = libraryPage.getContent();

        List<LibraryDto.Response> responseDto = mapper.librariesToResponses(libraryList);
        return ResponseCreator.list(responseDto, libraryPage.getTotalPages());
    }

    @DeleteMapping("/{library-id}")
    public ResponseEntity<Void> delete(@PathVariable("library-id") Long Id) {

        service.delete(Id);

        return ResponseCreator.deleted();
    }


}
