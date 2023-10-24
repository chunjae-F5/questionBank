package com.example.f5.category.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class TextBook extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "name", nullable = false)
    @Comment(value = "ex) 고등학교 사회 문화")
    private String name;

    @Column(name = "writer", nullable = false)
    @Comment(value = "ex) 우장희")
    private String writer;

    @Column(name = "curriculum", nullable = false)
    @Comment(value = "ex) 2015개정 교육과정")
    private String curriculum;
}
