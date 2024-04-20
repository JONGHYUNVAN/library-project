package com.demo.library.dbController.SampleInserter;

import com.demo.library.book.entity.BookEntity;
import com.demo.library.book.repository.BookJPARepository;
import com.demo.library.book.repository.BookRepository;
import com.demo.library.book.service.BookService;
import com.demo.library.dbController.refreshtokencleaner.ExpiredRefreshTokenCleaner;
import com.demo.library.genre.entity.Genre;
import com.demo.library.genre.entity.UserGenre;
import com.demo.library.genre.repository.GenreRepository;
import com.demo.library.genre.repository.UserGenreRepository;
import com.demo.library.library.entity.Library;
import com.demo.library.library.repository.LibraryRepository;
import com.demo.library.security.utils.AuthorityUtils;
import com.demo.library.user.entity.User;
import com.demo.library.user.repository.UserRepository;
import com.demo.library.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Random;


import java.util.*;

import static com.demo.library.user.entity.User.Status.ACTIVE;

@Component
@RequiredArgsConstructor
public class SampleInserter implements CommandLineRunner {
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final UserGenreRepository userGenreRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityUtils authorityUtils;
    private final ExpiredRefreshTokenCleaner expiredRefreshTokenCleaner;
    @Value("${mail.address.admin}")
    private String adminMailAddress;
    @Value("${password.address.admin}")
    private String password;

    @Override
    public void run (String ... args){
        Optional<User> optionalUser = userRepository.findById(1L);
        if(optionalUser.isEmpty()){
            Genre literatureArt = createGenre(1L,"Literature & Art");
            Genre scienceTechnology = createGenre(2L,"Science & Technology");
            Genre societyPolitics = createGenre(3L,"Society & Politics");
            Genre economyBusiness = createGenre(4L,"Economy & Business");
            Genre selfImprovementLife = createGenre(5L,"Self-improvement & Life");

            List<Genre> genreList = List.of(
                    literatureArt,scienceTechnology,societyPolitics,economyBusiness,selfImprovementLife
            );
            genreRepository.saveAll(genreList);

            User user = createUser(1L, password, adminMailAddress, "관리자", "admin", "010-1234-5678", User.Gender.MALE, User.Status.ACTIVE);
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            List<String> roles = authorityUtils.createRoles(user.getEmail());
            user.setRoles(roles);
            user.setStatus(ACTIVE);
            userRepository.save(user);

            Library library = createLibrary(1L, "중앙도서관", "서울시 구구구 동동동 로로로 77-7", "09:30", "17:30");
            libraryRepository.save(library);

            List<BookEntity> bookList = List.of(
                    createBook(1L, "위대한 개츠비", "우리출판사", "F. Scott Fitzgerald", library, BookEntity.Status.AVAILABLE, 1L, "https://i.namu.wiki/i/od3GZzcq3QGTDEKEACznhtMZYH1vRIotrxMXCpubeRGDWli57WEDgm9rYmZZhiVqcPp72wrR3UbyTR1yl7DUUg.webp",literatureArt),
                    createBook(2L, "호밀밭의 파수꾼", "Moonlight Publishers", "J. D. Salinger", library, BookEntity.Status.AVAILABLE, 1L, "https://sophialearning.s3.amazonaws.com/packet_logos/57302/large/holden-1.jpg",literatureArt),
                    createBook(3L, "앵무새 죽이기", "Sunset Books", "Harper Lee", library, BookEntity.Status.AVAILABLE, 1L, "https://image.aladin.co.kr/product/37/55/cover500/8931001991_2.jpg",literatureArt),
                    createBook(4L, "1984", "Starlight Publishing", "George Orwell", library, BookEntity.Status.AVAILABLE, 1L, "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/f36h/image/dh_2xYf8yd6XsmPv2CbMws5xWGk.jpg",literatureArt),
                    createBook(5L, "오만과 편견", "아침이슬 출판사", "Jane Austen", library, BookEntity.Status.AVAILABLE, 1L, "https://tkfile.yes24.com/upload2/PerfBlog/202008/20200812/20200812-37327_01.jpg",literatureArt),
                    createBook(6L, "호빗", "마운틴 피크 북스", "J. R. R. 톨킨", library, BookEntity.Status.AVAILABLE, 1L, "https://cdn.ilyoseoul.co.kr/news/photo/201301/78433_35363_1515.jpg",literatureArt),
                    createBook(7L, "등대로", "Ocean Breeze Publishing", "Virginia Woolf", library, BookEntity.Status.AVAILABLE, 1L, "https://contents.kyobobook.co.kr/sih/fit-in/280x0/pdt/9788949718170.jpg",literatureArt),
                    createBook(8L, "모비딕", "Seaside Publishers", "Herman Melville", library, BookEntity.Status.AVAILABLE, 1L, "https://contents.kyobobook.co.kr/sih/fit-in/400x0/pdt/9788983797124.jpg",literatureArt),
                    createBook(9L, "멋진 신세계", "Horizon Books", "Aldous Huxley", library, BookEntity.Status.AVAILABLE, 1L, "https://file2.nocutnews.co.kr/newsroom/image/2016/04/04/20160404161817975754.jpg",literatureArt),
                    createBook(10L, "반지의 제왕", "Twilight Publications", "J. R. R. Tolkien", library, BookEntity.Status.AVAILABLE, 1L, "https://t1.daumcdn.net/movie/148186da3dcb2911fd44279942e23e1249f8147f",literatureArt),
                    createBook(11L, "변신", "Penguin Classics", "Franz Kafka", library, BookEntity.Status.AVAILABLE, 0L, "https://qi-b.qoo10cdn.com/partner/goods_image/5/5/6/7/354935567g.jpg",literatureArt),
                    createBook(12L, "Two Can Keep a Secret", "Delacorte Press", "Karen M. McManus", library, BookEntity.Status.AVAILABLE, 0L, "https://cdn.gramedia.com/uploads/items/9786020627632_Two_Can_Keep_.jpg",literatureArt),
                    createBook(13L, "이방인", "Gallimard", "Albert Camus", library, BookEntity.Status.AVAILABLE, 0L, "https://i.namu.wiki/i/9dVZ6CpBuGtbfgnhw48HVenjAC6bS89MvrhyxEy6nSmeE-cwO3ghNVcnZNamBAe7-eysYaO50UNOTYeeAAXQmQ.webp",literatureArt),
                    createBook(14L, "나미야 잡화점의 기적", "Doubleday", "Higashino Keigo", library, BookEntity.Status.AVAILABLE, 0L, "https://m.media-amazon.com/images/I/81WYIvrWsEL._AC_UF1000,1000_QL80_.jpg",literatureArt),
                    createBook(15L, "사랑의 묘약", "Simon & Schuster", "Sarah Dessen", library, BookEntity.Status.AVAILABLE, 0L, "https://media-storage-production.s3-eu-central-1.amazonaws.com/c82b7c44-d8d6-4eba-905e-e67552dfb16f/987fc5d6-6054-4467-ab4b-f9c00b719824.png",literatureArt),
                    createBook(16L, "캣츠", "HarperCollins", "T. S. Eliot", library, BookEntity.Status.AVAILABLE, 0L, "https://image.yes24.com/momo/TopCate48/MidCate05/4742146.jpg",literatureArt),
                    createBook(17L, "겨울왕국", "Puffin Books", "Disney", library, BookEntity.Status.AVAILABLE, 0L, "https://www.bookxcess.com/cdn/shop/products/9781472325204_1_d4c969df-cf0e-4668-b0eb-514512b057f0.jpg?v=1649883633",literatureArt),
                    createBook(18L, "해리 포터와 마법사의 돌", "Bloomsbury", "J. K. Rowling", library, BookEntity.Status.AVAILABLE, 0L, "https://m.media-amazon.com/images/I/91wKDODkgWL._AC_UF1000,1000_QL80_.jpg",literatureArt),
                    createBook(19L, "파우스트", "Vintage", "Johann Wolfgang von Goethe", library, BookEntity.Status.AVAILABLE, 0L, "https://www.lgart.com/Down/Perf/202302/faust_750-983(0222).png",literatureArt),
                    createBook(20L, "어린 왕자", "Reynal & Hitchcock", "A.Saint-Exupéry", library, BookEntity.Status.AVAILABLE, 0L, "https://espacemalraux.jouelestours.fr/sites/default/files/espacemalraux/styles/galerie_photos/public/ged/visuel_petit_prince.jpeg?itok=47HHkKYB",literatureArt),
                    createBook(21L, "빨간 머리 앤", "Puffin Classics", "L. M. Montgomery", library, BookEntity.Status.AVAILABLE, 0L, "https://dimg.donga.com/wps/NEWS/IMAGE/2019/09/27/97602605.3.jpg",literatureArt),
                    createBook(22L, "레미제라블", "Signet Classics", "Victor Hugo", library, BookEntity.Status.AVAILABLE, 0L, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbiSw3nqtArRPm9-SDzB1KL_VXh6-AiKYOm6xeXVw-S6NQBEb5QplsZUzy-Q3EczdoR38&usqp=CAU",literatureArt),
                    createBook(23L, "메트로폴리스", "Eos", "Thea von Harbou", library, BookEntity.Status.AVAILABLE, 0L, "https://images.justwatch.com/poster/210180905/s592/meteuropolriseu",literatureArt),
                    createBook(24L, "노인과 바다", "Scribner", "Ernest Hemingway", library, BookEntity.Status.AVAILABLE, 0L, "https://www.koya-culture.com/data/photos/20230730/art_16904994059296_87c3a8.jpg",literatureArt),
                    createBook(25L, "로미오와 줄리엣", "Oxford University", "William Shakespeare", library, BookEntity.Status.AVAILABLE, 0L, "https://www.adaptivereader.com/cdn/shop/files/Romeo_Juliet.png?v=1686691995",literatureArt),
                    createBook(26L, "피노키오", "Puffin Classics", "Carlo Collodi", library, BookEntity.Status.AVAILABLE, 0L, "https://data.onnada.com/anime/202310/2042590192_6d143837_00f30.jpeg",literatureArt),
                    createBook(27L, "이상한 나라의 앨리스", "Macmillan", "Lewis Carroll", library, BookEntity.Status.AVAILABLE, 0L, "https://play-lh.googleusercontent.com/proxy/Bas4UFlOJtC6TICWW_YJvWNTU1mtOU-wN0dBlJoVqwKy2yI21weBb3P17rGE7cikagmUDqEXZK-PRcXbYuB2i7klfEGjM4M47QNKlA5ybvLfv49Ad5SqmhTvjqFahVyvv-oC_bcVRk1z38Vyc2RL-CHF4tUauCN4gIxUJQ=w240-h480-rw",literatureArt),
                    createBook(28L, "프랑켄슈타인", "Lackington, Hughes", "Mary Shelley", library, BookEntity.Status.AVAILABLE, 0L, "https://img.freepik.com/premium-photo/the-monster-of-frankenstein-headshot_73899-6957.jpg",literatureArt),
                    createBook(29L, "토지", "Changbi", "Park Kyung-ni", library, BookEntity.Status.AVAILABLE, 0L, "https://i.namu.wiki/i/d5Y8XXzIOdFQHLca0yKLvQL_gC7b2xDWHAwNIgK7ESYP1pI7m3YP6n_RZKCeJCC38CrI3yModNoiYLbjy5ULPQ.webp",literatureArt),
                    createBook(30L, "잃어버린 시간을 찾아서", "Harper & Row", "Marcel Proust", library, BookEntity.Status.AVAILABLE, 0L, "https://d16q17a0zuuij1.cloudfront.net/books/58839/chapters/download/504577/w/1200/eb230131_1440.jpg",literatureArt),
                    createBook(31L, "중요한 질문, 간결한 대답", "Bantam Books", "Stephen Hawking", library, BookEntity.Status.AVAILABLE, 0L, "https://dbbooks.co.kr/image/img/9781473695993.jpg",scienceTechnology),
                    createBook(32L, "사피엔스", "Harvill Secker", "Yuval Noah Harari", library, BookEntity.Status.AVAILABLE, 0L, "https://image.aladin.co.kr/product/5998/26/cover500/0062316117_1.jpg", societyPolitics),
                    createBook(33L, "린 스타트업", "Crown Publishing Group", "Eric Ries", library, BookEntity.Status.AVAILABLE, 0L, "https://m.media-amazon.com/images/I/61BFOf9Ap-L._AC_UF1000,1000_QL80_.jpg", economyBusiness),
                    createBook(34L, "일곱 가지 습관", "Free Press", "Stephen R. Covey", library, BookEntity.Status.AVAILABLE, 0L, "https://d28hgpri8am2if.cloudfront.net/book_images/onix/cvr9781442350816/the-7-habits-of-highly-effective-people-9781442350816_hr.jpg", selfImprovementLife)
            );
            bookRepository.saveAll(bookList);

            Set<Long> genreIds = new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
            bookList.stream()
                    .filter(book -> book.getGenre() != null && genreIds.contains(book.getGenre().getId()))
                    .forEach(book -> {
                        List<BookEntity> books = Optional.ofNullable(book.getGenre().getBooks()).orElse(new ArrayList<>());
                        books.add(book);
                        book.getGenre().setBooks(books);
                    });

            genreRepository.saveAll(genreList);
            List<UserGenre> userGenres = List.of(
                    createUserGenre(1L,user,literatureArt),
                    createUserGenre(2L,user,scienceTechnology),
                    createUserGenre(3L,user,societyPolitics),
                    createUserGenre(4L,user,economyBusiness),
                    createUserGenre(5L,user,selfImprovementLife)
            );
            userGenreRepository.saveAll(userGenres);
        }
        else expiredRefreshTokenCleaner.deleteExpiredTokens();
    }

    public BookEntity createBook(Long id, String title, String publisher, String author, Library library, BookEntity.Status status, Long searchCount, String imageURL,Genre genre){

        return BookEntity.builder()
                .id(id)
                .title(title)
                .publisher(publisher)
                .author(author)
                .library(library)
                .status(status)
                .searchCount(searchCount)
                .imageURL(imageURL)
                .genre(genre)

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
                .profile(0)
                .status(status)

                .build();
    }
    public Genre createGenre(Long id, String name){
        return Genre.builder()
                .id(id)
                .name(name)
                .build();
    }
    public UserGenre createUserGenre(Long id,User user, Genre genre){
        Random rand = new Random();
        return UserGenre.builder()
                .id(id)
                .user(user)
                .genre(genre)
                .searched(rand.nextLong(30) + 1)
                .loaned(rand.nextLong(30) + 1)

                .build();
    }

}
