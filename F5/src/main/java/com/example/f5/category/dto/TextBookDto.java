package com.example.f5.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class TextBookDto {
    @AllArgsConstructor
    @Getter
    public static class ResponseDto {
        private int itemId;
        private String imgUrl;
        private String name;
        private String writer;
        private String curriculum;
    }
}
