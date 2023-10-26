package com.example.f5.exam.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "number", nullable = false)
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

    @Column(name = "content_file", nullable = false)
    @Comment(value = "지문 이미지 경로")
    private String contentFile;

    @Column(name = "question_file", nullable = false)
    @Comment(value = "문제 사진 경로 url")
    private String questionFile;
}
