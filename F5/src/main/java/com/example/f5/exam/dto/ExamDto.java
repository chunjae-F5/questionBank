package com.example.f5.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        private Long subjectId;
        private String subjectName;
        private Long largeChapterId;
        private String largeChapterName;
        private Long mediumChapterId;
        private String mediumChapterName;
        private Long smallChapterId;
        private String smallChapterName;
        private Long topicChapterId;
        private String topicChapterName;
    }

   @Getter
   @Setter
    public static class Curriculum {
        public String curriculumCode;
        public String curriculumName;
        public Long subjectId;
        public String subjectName;
        public List<LargeChapter> chapters = new ArrayList<>();

       @Getter
       @Setter
       public static class LargeChapter {
           public Long largeChapterId;
           public String largeChapterName;
           public List<MediumChapter> mediumChapters = new ArrayList<>();

           @Override
           public boolean equals(Object o) {
               if (this == o) return true;
               if (o == null || getClass() != o.getClass()) return false;
               ExamDto.Curriculum.LargeChapter that = (ExamDto.Curriculum.LargeChapter) o;
               return Objects.equals(largeChapterId, that.largeChapterId);
           }

           @Override
           public int hashCode() {
               return Objects.hash(largeChapterId);
           }

           @Getter
           @Setter
           public static class MediumChapter {
               public Long mediumChapterId;
               public String mediumChapterName;
               public List<SmallChapter> smallChapters = new ArrayList<>();

               @Override
               public boolean equals(Object o) {
                   if (this == o) return true;
                   if (o == null || getClass() != o.getClass()) return false;
                   ExamDto.Curriculum.LargeChapter.MediumChapter that = (ExamDto.Curriculum.LargeChapter.MediumChapter) o;
                   return Objects.equals(mediumChapterId, that.mediumChapterId);
               }

               @Override
               public int hashCode() {
                   return Objects.hash(mediumChapterId);
               }

               @Getter
               @Setter
               public static class SmallChapter {
                   public Long smallChapterId;
                   public String smallChapterName;
                   public List<TopicChapter> topicChapters = new ArrayList<>();

                   @Override
                   public boolean equals(Object o) {
                       if (this == o) return true;
                       if (o == null || getClass() != o.getClass()) return false;
                       ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter that = (ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter) o;
                       return Objects.equals(smallChapterId, that.smallChapterId);
                   }

                   @Override
                   public int hashCode() {
                       return Objects.hash(smallChapterId);
                   }

                   @Getter
                   @Setter
                   public static class TopicChapter {
                       public Long topicChapterId;
                       public String topicChapterName;

                       @Override
                       public boolean equals(Object o) {
                           if (this == o) return true;
                           if (o == null || getClass() != o.getClass()) return false;
                           ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter.TopicChapter that = (ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter.TopicChapter) o;
                           return Objects.equals(topicChapterId, that.topicChapterId);
                       }

                       @Override
                       public int hashCode() {
                           return Objects.hash(topicChapterId);
                       }
                   }
               }
           }
       }
    }
}
