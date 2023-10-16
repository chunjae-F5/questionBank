package com.example.f5.exam.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class Exam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "highest", nullable = false)
    @Max(value = 50)
    @Comment(value = "최상 난이도 개수")
    private String highest;

    @Column(name = "high", nullable = false)
    @Max(value = 50)
    @Comment(value = "상 난이도 개수")
    private String high;

    @Column(name = "medium", nullable = false)
    @Max(value = 50)
    @Comment(value = "중 난이도 개수")
    private String medium;

    @Column(name = "low", nullable = false)
    @Max(value = 50)
    @Comment(value = "하 난이도 개수")
    private String low;

    @Column(name = "total", nullable = false)
    @Max(value = 50)
    @Comment(value = "총 문제 개수")
    private String total;

    @Column(name = "choice_answer", nullable = false)
    @Max(value = 50)
    @Comment(value = "객관식 문제 개수")
    private int choiceAnswer;

    @Column(name = "short_answer", nullable = false)
    @Max(value = 50)
    @Comment(value = "주관식 문제 개수")
    private int shortAnswer;

    @Column(name = "long_answer", nullable = false)
    @Max(value = 50)
    @Comment(value = "서술형 문제 개수")
    private int longAnswer;
}
