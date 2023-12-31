package com.example.f5.exam.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
public class Exam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "highest", nullable = false)
    @Comment(value = "최상 난이도 개수")
    private int highest;

    @Column(name = "high", nullable = false)
    @Comment(value = "상 난이도 개수")
    private int high;

    @Column(name = "medium", nullable = false)
    @Comment(value = "중 난이도 개수")
    private int medium;

    @Column(name = "low", nullable = false)
    @Comment(value = "하 난이도 개수")
    private int low;

    @Column(name = "total", nullable = false)
    @Comment(value = "총 문제 개수")
    private int total;

    @Column(name = "choice_answer", nullable = false)
    @Comment(value = "객관식 문제 개수")
    private int choiceAnswer;

    @Column(name = "short_answer", nullable = false)
    @Comment(value = "주관식 문제 개수")
    private int shortAnswer;

    @Column(name = "long_answer", nullable = false)
    @Comment(value = "서술형 문제 개수")
    private int longAnswer;
}
