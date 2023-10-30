package com.example.f5.exam.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamArchiveListDTO {

    private int idx;
    private int total;
    private LocalDateTime createdDate;
    private String flag;
    private String grade;
    private String name;
    private String question;
    private String userId;

    public ExamArchiveListDTO(int idx, int total, LocalDateTime createdDate, String flag, String grade, String name, String question, String userId) {
        this.idx = idx;
        this.total = total;
        this.createdDate = createdDate;
        this.flag = flag;
        this.grade = grade;
        this.name = name;
        this.question = question;
        this.userId = userId;
    }

}
