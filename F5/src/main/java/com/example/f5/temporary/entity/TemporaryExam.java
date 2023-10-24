package com.example.f5.temporary.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class TemporaryExam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int idx;

    @ManyToOne
    @JoinColumn(name = "question_idx")
    private TemporaryContent questionIdx;

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

    @Column(name = "user_id", nullable = false, length = 50)
    @Comment(value = "유저명")
    private String userId;
}
