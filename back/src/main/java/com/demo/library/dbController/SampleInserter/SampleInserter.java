package com.demo.library.dbController.SampleInserter;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.book.repository.BookJPARepository;
import com.demo.library.book.service.BookService;
import com.demo.library.library.entity.Library;
import com.demo.library.library.repository.LibraryRepository;
import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import com.demo.library.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SampleInserter implements CommandLineRunner {
    private final BookJPARepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    @Value("${mail.address.admin}")
    private String adminMailAddress;
    @Value("${password.address.admin}")
    private String password;

    @Override
    public void run (String ... args){
        Optional<User> optionalUser = userRepository.findById(1L);
        if(optionalUser.isEmpty()){
            User user = createUser(1L, password, adminMailAddress, "관리자", "admin", "010-1234-5678", User.Gender.MALE, User.Status.ACTIVE);
            userService.create(user);

            Library library = createLibrary(1L, "중앙도서관", "서울시 구구구 동동동 로로로 77-7", "09:30", "17:30");
            libraryRepository.save(library);

            List<BookEntity> bookList = List.of(
                    createBook(1L, "위대한 개츠비", "우리출판사", "F. Scott Fitzgerald", library, BookEntity.Status.AVAILABLE, 1L, "https://i.namu.wiki/i/od3GZzcq3QGTDEKEACznhtMZYH1vRIotrxMXCpubeRGDWli57WEDgm9rYmZZhiVqcPp72wrR3UbyTR1yl7DUUg.webp"),
                    createBook(2L, "호밀밭의 파수꾼", "Moonlight Publishers", "J. D. Salinger", library, BookEntity.Status.AVAILABLE, 1L, "https://sophialearning.s3.amazonaws.com/packet_logos/57302/large/holden-1.jpg"),
                    createBook(3L, "앵무새 죽이기", "Sunset Books", "Harper Lee", library, BookEntity.Status.AVAILABLE, 1L, "https://image.aladin.co.kr/product/37/55/cover500/8931001991_2.jpg"),
                    createBook(4L, "1984", "Starlight Publishing", "George Orwell", library, BookEntity.Status.AVAILABLE, 1L, "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/f36h/image/dh_2xYf8yd6XsmPv2CbMws5xWGk.jpg"),
                    createBook(5L, "오만과 편견", "아침이슬 출판사", "Jane Austen", library, BookEntity.Status.AVAILABLE, 1L, "https://tkfile.yes24.com/upload2/PerfBlog/202008/20200812/20200812-37327_01.jpg"),
                    createBook(6L, "호빗", "마운틴 피크 북스", "J. R. R. 톨킨", library, BookEntity.Status.AVAILABLE, 1L, "https://cdn.ilyoseoul.co.kr/news/photo/201301/78433_35363_1515.jpg"),
                    createBook(7L, "등대로", "Ocean Breeze Publishing", "Virginia Woolf", library, BookEntity.Status.AVAILABLE, 1L, "https://contents.kyobobook.co.kr/sih/fit-in/280x0/pdt/9788949718170.jpg"),
                    createBook(8L, "모비딕", "Seaside Publishers", "Herman Melville", library, BookEntity.Status.AVAILABLE, 1L, "https://contents.kyobobook.co.kr/sih/fit-in/400x0/pdt/9788983797124.jpg"),
                    createBook(9L, "멋진 신세계", "Horizon Books", "Aldous Huxley", library, BookEntity.Status.AVAILABLE, 1L, "https://file2.nocutnews.co.kr/newsroom/image/2016/04/04/20160404161817975754.jpg"),
                    createBook(10L, "반지의 제왕", "Twilight Publications", "J. R. R. Tolkien", library, BookEntity.Status.AVAILABLE, 1L, "https://t1.daumcdn.net/movie/148186da3dcb2911fd44279942e23e1249f8147f"),
                    createBook(11L, "오만과 편견", "Penguin Classics", "Jane Austen", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(12L, "투 캔 킵 어 시크릿", "Delacorte Press", "Karen M. McManus", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(13L, "이방인", "Gallimard", "Albert Camus", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(14L, "나미야 잡화점의 기적", "Doubleday", "Higashino Keigo", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(15L, "사랑의 묘약", "Simon & Schuster", "Sarah Dessen", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(16L, "캣츠", "HarperCollins", "T. S. Eliot", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(17L, "겨울왕국", "Puffin Books", "Disney", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(18L, "해리 포터와 마법사의 돌", "Bloomsbury", "J. K. Rowling", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(19L, "파우스트", "Vintage", "Johann Wolfgang von Goethe", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(20L, "피터 팬", "Hodder & Stoughton", "J. M. Barrie", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(21L, "빨간 머리 앤", "Puffin Classics", "L. M. Montgomery", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(22L, "레미제라블", "Signet Classics", "Victor Hugo", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(23L, "메트로폴리스", "Eos", "Thea von Harbou", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(24L, "노인과 바다", "Scribner", "Ernest Hemingway", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(25L, "로미오와 줄리엣", "Oxford University", "William Shakespeare", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(26L, "피노키오", "Puffin Classics", "Carlo Collodi", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(27L, "앨리스 원더랜드", "Macmillan", "Lewis Carroll", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(28L, "프랑켄슈타인", "Lackington, Hughes", "Mary Shelley", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(29L, "토지", "Changbi", "Park Kyung-ni", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com"),
                    createBook(30L, "캔디", "Harper & Row", "Terry Southern", library, BookEntity.Status.AVAILABLE, 0L, "https://www.imageURL.com")
            );
            bookRepository.saveAll(bookList);
        }
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

    public User createUser (Long id,String password, String email, String name, String nickName, String phoneNumber, User.Gender gender, User.Status status ){
        return User.builder()
                .id(id)
                .password(password)
                .email(email)
                .name(name)
                .nickName(nickName)
                .phoneNumber(phoneNumber)
                .gender(gender)

                .status(status)

                .build();
    }

}
