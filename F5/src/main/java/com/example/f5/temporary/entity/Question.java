package com.example.f5.temporary.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "number", nullable = false)
    @Max(value = 50)
    @Comment(value = "문제번호")
    private int number;   //문제번호

    @Column(name = "type", nullable = false)
    @Comment(value = "ex) 1. 100까지의 수>1. 몇십 알아보기>1.60,70,80,90 알아보기>1.60,70,80,90,을 묶음으로 알아보기")
    private String type;

    @Column(name = "form", nullable = false, length = 50)
    @Comment(value = "ex) 주관식 (객관식, 서술형)")
    private String form;

    @Column(name = "level", nullable = false, length = 10)
    @Comment(value = "ex) 상 (중, 하)")
    private String level;

    @Column(name = "question_file", nullable = false)
    @Comment(value = "문제 사진 경로 url")
    private String questionFile;
}
