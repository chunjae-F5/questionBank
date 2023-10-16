package com.example.f5.exam.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class Archive extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "type", nullable = false)
    @Comment(value = "유형")
    private String type;

    @Column(name = "grade", nullable = false, length = 50)
    @Comment(value = "학년")
    private String grade;

    @Column(name = "name", nullable = false)
    @Comment(value = "문제지명")
    private String name;

    @Column(name = "total", nullable = false)
    @Comment(value = "총 문제 수")
    private int total;

    @Column(name = "question", nullable = false)
    @Comment(value = "문제지")
    private String question;

    @Column(name = "answer", nullable = false)
    @Comment(value = "해설지")
    private String answer;

    @Column(name = "distributor", nullable = false)
    @Comment(value = "배포받은 사람")
    private String distributor; //배포 받은 사람

    @Column(name = "flag", nullable = false, length = 50)
    @Comment(value = "분류 (H: 고 M: 중 E:초)")
    private String flag;
}
