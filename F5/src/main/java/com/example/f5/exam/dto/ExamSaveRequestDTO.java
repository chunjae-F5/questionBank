package com.example.f5.exam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
@Setter
public class ExamSaveRequestDTO {

        private List<ExamSaveRequestDTO.ProcessedData> processedData;
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
        public ExamSaveRequestDTO(){}

        @Getter
        @Setter
        public static class ProcessedData{
                private int idx;
                private int number;
                private String type;
                private String questionFormName;
                private String difficultyName;
                private String questionUrl;
                private String passageUrl;
                private int passageId;

                public ProcessedData(){}

        }


}
