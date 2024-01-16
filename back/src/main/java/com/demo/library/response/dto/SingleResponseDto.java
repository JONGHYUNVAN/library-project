package com.demo.library.response.dto;

import com.demo.library.response.DtoSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SingleResponseDto<T> {
    @JsonSerialize(using = DtoSerializer.class)
    private T data;
}
