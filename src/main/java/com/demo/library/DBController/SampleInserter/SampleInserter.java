package com.demo.library.DBController.SampleInserter;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.book.repository.BookJPARepository;
import com.demo.library.library.entity.Library;
import com.demo.library.library.repository.LibraryRepository;
import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SampleInserter implements CommandLineRunner {
    private final BookJPARepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;

    @Override
    public void run (String ... args){
        User user = createUser(1L, "김도서", "bookKim","010-1234-5678", User.Gender.MALE, User.Status.ACTIVE);
        userRepository.save(user);

      Library library = createLibrary(1L, "중앙도서관","서울시 구구구 동동동 로로로 77-7","09:30","17:30");
      libraryRepository.save(library);

        List<BookEntity> bookList = List.of(
                createBook(1L, "위대한 개츠비", "우리출판사", "F. Scott Fitzgerald", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                createBook(2L, "호밀밭의 파수꾼", "Moonlight Publishers", "J. D. Salinger", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                createBook(3L, "앵무새 죽이기", "Sunset Books", "Harper Lee", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                createBook(4L, "1984", "Starlight Publishing", "George Orwell", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                createBook(5L, "오만과 편견", "아침이슬 출판사", "Jane Austen", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                createBook(6L, "호빗", "마운틴 피크 북스", "J. R. R. 톨킨", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                createBook(7L, "등대로", "Ocean Breeze Publishing", "Virginia Woolf", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                createBook(8L, "모비딕", "Seaside Publishers", "Herman Melville", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                createBook(9L, "멋진 신세계", "Horizon Books", "Aldous Huxley", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                createBook(10L, "반지의 제왕", "Twilight Publications", "J. R. R. Tolkien", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com")
        );
        bookRepository.saveAll(bookList);

    }



    public BookEntity createBook(Long id, String title, String publisher, String author, Library library, BookEntity.Status status, Long searchCount, String imageURL ){

        return BookEntity.builder()
                .id(id)
                .title(title)
                .publisher(publisher)
                .author(author)
                .library(library)
                .status(status)
                .searchCount(searchCount)
                .imageURL(imageURL)

                .build();
    }
    public Library createLibrary(Long id, String name, String address,String openTime,String closeTime){
        return  Library.builder()
                .id(id)
                .name(name)
                .address(address)
                .openTime(openTime)
                .closeTime(closeTime)

                .build();
    }

    public User createUser (Long id, String name, String nickName, String phoneNumber, User.Gender gender, User.Status status ){
        return User.builder()
                .id(id)
                .name(name)
                .nickName(nickName)
                .phoneNumber(phoneNumber)
                .gender(gender)

                .status(status)

                .build();
    }

}
