package com.example.f5.exam.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
public class Archive extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "user_id", nullable = false)
    @Comment(value = "유저 아이디")
    private String userId;

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

    @Column(name = "preview_img", nullable = false)
    @Comment(value = "미리보기 이미지")
    private String previewImg;

    @Column(name = "flag", nullable = false, length = 50)
    @Comment(value = "분류 (H: 고 M: 중 E:초)")
    private String flag;
}
