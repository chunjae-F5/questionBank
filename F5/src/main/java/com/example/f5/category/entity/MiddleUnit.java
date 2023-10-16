package com.example.f5.category.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class MiddleUnit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @ManyToOne
    @JoinColumn(name = "small_unit_idx")
    private SmallUnit smallUnitIdx;

    @Column(name = "name", nullable = false)
    @Comment(value = "ex) 1. 소수와 합성수")
    private String name;
}