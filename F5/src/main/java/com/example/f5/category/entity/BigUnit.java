package com.example.f5.category.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class BigUnit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @ManyToOne
    @JoinColumn(name = "middle_unit_idx")
    private MiddleUnit middleUnitIdx;

    @Column(name = "name", nullable = false)
    @Comment(value = "ex) Ⅰ. 소인수분해")
    private String name;
}
