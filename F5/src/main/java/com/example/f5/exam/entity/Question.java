package com.example.f5.exam.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;

    private String sequence;
    private String category;
    private String form;
    private String difficulty;
    private String question;
}
