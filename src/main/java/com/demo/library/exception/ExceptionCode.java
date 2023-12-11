package com.demo.library.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionCode {
    USER_NOT_FOUND(404, "User not found"),
    USER_ALREADY_EXISTS(409, "User already exists"),
    NOT_IMPLEMENTATION(501, "Not Implementation"),
    INVALID_USER_ID(400, "Invalid user Id"),
    INVALID_USER_EMAIL(400, "Invalid user Email"),
    INVALID_USER_PHONE_NUMBER(400, "Invalid phone number"),
    INVALID_USER_STATUS(400, "Invalid user status"),
    LOAN_EXISTS(409,"Loan exists"),

    BOOK_NOT_FOUND(404, "Book not found"),
    BOOK_ALREADY_EXISTS(409, "Book already exists"),
    INVALID_BOOK_ID(400, "Invalid book Id"),
    BOOK_IS_ON_LOAN(400, "Book is on loan"),

    LOAN_NOT_FOUND(404,"Loan not found"),
    INVALID_LOAN_ID(400, "Invalid loan Id"),
    USER_ID_NO_MATCH(402, "Invalid user Id"),
    BOOK_ID_NO_MATCH(402, "Invalid book Id"),
    HAS_OVERDUE_LOAN(402, "There is an overdue loan for the user"),
    USER_BANNED_FROM_LIBRARY(402, "The user is banned from library"),

    LIBRARY_ID_NOT_CONTAINED(404,"Library not found"),
    LIBRARY_ALREADY_EXISTS(409, "Library already exists"),
    INVALID_LIBRARY_ID(400, "Invalid library Id"),

    ACCESS_TOKEN_EXPIRED(404, "AccessToken Expired"),
    INVALID_TOKEN_REQUEST(404, "Invalid Token Request"),
    REFRESH_TOKEN_NOT_FOUND(404, "RefreshToken Not Found"),
    REFRESH_TOKEN_EXPIRED(404, "Refresh Token Expired");


    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
