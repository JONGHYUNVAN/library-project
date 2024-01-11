package com.demo.library.library.service;



import com.demo.library.library.repository.LibraryRepository;
import com.demo.library.exception.BusinessLogicException;
import com.demo.library.utils.EntityUpdater;
import lombok.RequiredArgsConstructor;
import com.demo.library.library.entity.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.util.*;

import static com.demo.library.exception.ExceptionCode.*;


@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final EntityUpdater<Library> entityUpdater;


    public Library create(Library Library) {
        isExistName(Library.getName());

        return libraryRepository.save(Library);
    }

    public Library update(Library library) {
        Long libraryId = library.getId();
        Library verifiedLibrary = verifyById(libraryId);

        entityUpdater.update(library, verifiedLibrary, Library.class);

        return libraryRepository.save(verifiedLibrary);
    }

    public Library get(long Id) {
        return libraryRepository.findById(Id).orElseThrow(() -> {
            throw new BusinessLogicException(LIBRARY_ID_NOT_CONTAINED);
        });
    }

    public void delete(long Id) {
        Library verifiedLibrary = verifyById(Id);
        libraryRepository.delete(verifiedLibrary);
    }
    // Inner Methods



    public void isExistName(String name) {
        Optional<Library> optionalLibrary = libraryRepository.findByName(name);
        optionalLibrary.ifPresent(s -> {
            throw new BusinessLogicException(LIBRARY_ALREADY_EXISTS);
        });
    }

    public Library verifyById(Long libraryId) {
        return libraryRepository.findById(libraryId)
                .orElseThrow(() -> {
                    throw new BusinessLogicException(INVALID_LIBRARY_ID);
                });
    }
    public void verifyId(Long Id){
        Optional.ofNullable(Id)
                .orElseThrow(() -> new BusinessLogicException(LIBRARY_ID_NOT_CONTAINED));
    }
    public Page<Library> getLibrariesByKeyword(String keyword, Pageable pageable) {
        PageRequest of = PageRequest.of(pageable.getPageNumber() ,
                pageable.getPageSize(),
                pageable.getSort());

        return libraryRepository.findAllByNameContaining(keyword,of);
    }

}
