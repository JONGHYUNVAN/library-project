package com.demo.library.response;

import com.demo.library.response.dto.ListResponseDto;
import com.demo.library.response.dto.SingleResponseDto;
import com.demo.library.user.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class ResponseCreator {
    public static ResponseEntity<Void> created(URI location) {
        return ResponseEntity.created(location).build();
    }
    public static <T> ResponseEntity<SingleResponseDto<T>> single(T response) {
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
    public static <T> ResponseEntity<ListResponseDto<T>> list(List<T> responses,int totalPages) {
        return new ResponseEntity<>(new ListResponseDto<>(responses,totalPages), HttpStatus.OK);
    }
    public static ResponseEntity<Void> deleted() {
        return ResponseEntity.noContent().build();
    }
}