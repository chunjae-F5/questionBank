package com.example.f5.temporary.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SimilarQueDto {

    @Setter
    @Getter
    public static class Request{
        private List<String> items;
    }
}
