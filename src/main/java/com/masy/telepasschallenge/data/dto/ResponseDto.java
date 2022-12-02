package com.masy.telepasschallenge.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto<T> {
    String message;
    T body;
}
