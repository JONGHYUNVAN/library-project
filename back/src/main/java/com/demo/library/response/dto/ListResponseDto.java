package com.demo.library.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class ListResponseDto<T> {
    private List<T> data;
    private  int totalPages;

}
