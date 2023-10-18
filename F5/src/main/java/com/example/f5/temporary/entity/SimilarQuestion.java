package com.example.f5.temporary.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;

@Entity
@Getter
@Table(name = "similar_question")
public class SimilarQuestion extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "form", nullable = false, length = 50)
    private String form;

    @Column(name = "level", nullable = false, length = 10)
    private String level;

    @Column(name = "question_file", nullable = false)
    private String questionFile;}
