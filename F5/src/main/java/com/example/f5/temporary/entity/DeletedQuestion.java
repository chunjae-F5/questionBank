package com.example.f5.temporary.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class DeletedQuestion extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deleted_question_idx", nullable = false)
    private int idx;

    @Column(name = "number", nullable = false)
    private short number;

    @Column(name = "level", nullable = false, length = 10)
    private String level;

    @Column(name = "form", nullable = false)
    private String form;

    @Column(name = "question_file", nullable = false)
    private String questionFile;
}