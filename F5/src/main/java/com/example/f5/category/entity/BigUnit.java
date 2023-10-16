package com.example.f5.category.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Entity
@Getter
public class BigUnit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "name", nullable = false)
    @Comment(value = "ex) Ⅰ. 소인수분해")
    private String name;
}
