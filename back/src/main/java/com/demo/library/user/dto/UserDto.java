package com.demo.library.user.dto;



import com.demo.library.genre.entity.UserGenre;
import com.demo.library.loanNban.dto.LoanDto;
import com.demo.library.utils.customValidator.notBlank.NotBlankValidation;
import com.demo.library.utils.customValidator.pattern.gender.GenderValidation;
import com.demo.library.utils.customValidator.pattern.notEmpty.NotEmptyValidation;
import com.demo.library.utils.customValidator.phoneNumberValidation.PhoneNumberValidation;
import com.demo.library.utils.customValidator.size.SizeValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

public class UserDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlankValidation(fieldName = "Name")
        @SizeValidation(max=20, fieldName = "Name")
        private String name;
        @NotBlankValidation(fieldName = "Password")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        private String password;
        @NotBlankValidation(fieldName = "Email")
        @SizeValidation(max=20, fieldName = "Email")
        @Email
        private String email;
        @NotBlankValidation(fieldName = "NickName")
        @SizeValidation(max=20, fieldName = "NickName")
        private String nickName;
        @NotBlankValidation(fieldName = "PhoneNumber")
        @PhoneNumberValidation
        private String phoneNumber;
        @NotBlank(message = "gender should be contained.")
        @GenderValidation
        private String gender;
        private int profile;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        @NotNull
        private Long id;
        @SizeValidation(max=20, fieldName = "NickName")
        private String nickName;
        private String password;
        @PhoneNumberValidation
        private String phoneNumber;
        private int profile;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;
        private String name;
        private String password;
        private String email;
        private String nickName;
        private String phoneNumber;
        private String gender;
        private List<LoanDto.Response> loans;
        private int profile;

    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class My{

        private Long id;
        private String name;
        private String email;
        private String nickName;
        private String phoneNumber;
        private String gender;
        private List<LoanDto.Response> loans;
        private List<MyUserGenre> genres;
        private int profile;

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyUserGenre{
        @JsonProperty("genre")
        private String name;
        private Long searched;
        private Long loaned;

    }
    @Getter
    public static class Request {
        @PhoneNumberValidation
        private String phoneNumber;

    }

}
