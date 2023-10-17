package com.example.f5.temporary.dto;

import com.example.f5.temporary.entity.TemporaryContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class TemporaryQuestionDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SaveDataDto {
            private TemporaryContent temporaryContentIdx;
            private int number;   //문제번호
            private String type;
            private String form;
            private String level;
            private String questionHtml;
            private String answerHtml;
            private String explainHtml;
    }
}
