package com.example.f5.temporary.service;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimilarObject {
    private long itemId;
    private String questionFormCode;
    private String difficultyName;
    private String chapterName;
    private String questionUrl;
    private String answerUrl;
    private String explainUrl;
}
