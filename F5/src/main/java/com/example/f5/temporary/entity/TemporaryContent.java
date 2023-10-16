package com.example.f5.temporary.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class TemporaryContent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_idx", nullable = false)
    private int idx;

    @Column(name = "content", nullable = false)
    @Comment(value = "지문 이미지 경로")
    private String contentFile;
}
