package com.example.f5.exam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExamSaveListDTO {
    @JsonProperty("itemList")
    private List<ItemInfo> itemList;
    @JsonProperty("successYn")
    private String successYn;

    @Getter
    @Setter
    public static class ItemInfo {
        private int itemNo;
        private int itemId;
        @JsonProperty("questionFormCode")
        private String questionFormCode;
        @JsonProperty("questionFormName")
        private String questionFormName;
//        @JsonProperty("difficultyCode")
//        private String difficultyCode;
        @JsonProperty("difficultyName")
        private String difficultyName;
//        @JsonProperty("largeChapterId")
//        private int largeChapterId;
        @JsonProperty("largeChapterName")
        private String largeChapterName;
//        @JsonProperty("mediumChapterId")
//        private int mediumChapterId;
        @JsonProperty("mediumChapterName")
        private String mediumChapterName;
//        @JsonProperty("smallChapterId")
//        private int smallChapterId;
//        @JsonProperty("smallChapterName")
//        private String smallChapterName;
//        @JsonProperty("topicChapterId")
//        private int topicChapterId;
//        @JsonProperty("topicChapterName")
//        private String topicChapterName;
        @JsonProperty("passageId")
        private int passageId;
        @JsonProperty("passageUrl")
        private String passageUrl;
        @JsonProperty("questionUrl")
        private String questionUrl;
//        @JsonProperty("answerUrl")
//        private String answerUrl;
//        @JsonProperty("explainUrl")
//        private String explainUrl;

        public ItemInfo () {}

    }

}
