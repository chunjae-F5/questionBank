package com.example.f5.exam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamSaveRequestDTO {

    private List<ProcessedData> processedData;
    private String examName;
    @JsonProperty("highCnt")
    private int high;
    @JsonProperty("mediumCnt")
    private int medium;
    @JsonProperty("lowCnt")
    private int low;
    @JsonProperty("choiceCnt")
    private int choiceAnswer;
    @JsonProperty("shortCnt")
    private int shortAnswer;
    @JsonProperty("subjectName")
    private String subjectName;

    public ExamSaveRequestDTO() {
    }

    @Getter
    @Setter
    public static class ProcessedData {
        private int idx;
        private int number;
        private String type;
        private String questionFormName;
        private String difficultyName;
        private String questionUrl;
        private String passageUrl;
        private int passageId;

        public ProcessedData() {
        }

    }


}
