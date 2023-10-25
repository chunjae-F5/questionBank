package com.example.f5.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ExamDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class itemInfoRequest {
        private List<Integer> activityCategoryList;
        private List<Integer> levelCnt;
        private List<MinorClassification> minorClassification;
        private String questionForm;

        @Getter
        @Setter
        public static class MinorClassification {
            private long large;
            private long medium;
            private long small;
            private long subject;
            private long topic;
        }
    }

    @Getter
    @Setter
    public static class itemInfoResponse {
        private long itemId;
        private String questionFormName;
        private String difficultyName;
        private String chapterName;
        private String questionUrl;
        private String answerUrl;
        private String explainUrl;
    }

    @Getter
    @Setter
    public static class CategoryResponse {
        private String curriculumCode;
        private String curriculumName;
        private int subjectId;
        private String subjectName;
        private int largeChapterId;
        private String largeChapterName;
        private int mediumChapterId;
        private String mediumChapterName;
        private int smallChapterId;
        private String smallChapterName;
        private int topicChapterId;
        private String topicChapterName;
    }

}
