package com.demo.library.response.dto;

import lombok.Getter;

import java.util.List;
@Getter
public class ListResponseDto<T> {
    private List<T> data;
    public ListResponseDto(List<T> data) {
        this.data = data;
    }

}
