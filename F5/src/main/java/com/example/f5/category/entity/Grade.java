package com.example.f5.category.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class Grade extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @ManyToOne
    @JoinColumn(name = "subject_idx")
    private Subject subjectIdx;

    @Column(name = "name", nullable = false)
    @Comment(value = "ex) 고1")
    private String name;
}
