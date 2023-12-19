package com.demo.library.user.dto;



import com.demo.library.loanNban.dto.LoanDto;
import com.demo.library.utils.customValidator.notBlank.NotBlankValidation;
import com.demo.library.utils.customValidator.pattern.gender.GenderValidation;
import com.demo.library.utils.customValidator.pattern.notEmpty.NotEmptyValidation;
import com.demo.library.utils.customValidator.phoneNumberValidation.PhoneNumberValidation;
import com.demo.library.utils.customValidator.size.SizeValidation;
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
        private String password;
        @NotBlankValidation(fieldName = "Email")
        @SizeValidation(max=20, fieldName = "Email")
        private String email;
        @NotBlankValidation(fieldName = "NickName")
        @SizeValidation(max=20, fieldName = "NickName")
        private String nickName;
        @PhoneNumberValidation
        private String phoneNumber;
        @NotBlank(message = "gender should be contained.")
        @GenderValidation
        private String gender;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private Long id;
        @NotEmptyValidation(fieldName = "Name")
        @SizeValidation(max=20, fieldName = "Name")
        private String name;
        @NotEmptyValidation(fieldName = "email")
        private String email;
        @NotEmptyValidation(fieldName = "NickName")
        @SizeValidation(max=20, fieldName = "NickName")
        private String nickName;

        @PhoneNumberValidation
        private String phoneNumber;
        @GenderValidation
        private String gender;

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

    }
    @Getter
    public static class Request {
        @PhoneNumberValidation
        private String phoneNumber;

    }

}
