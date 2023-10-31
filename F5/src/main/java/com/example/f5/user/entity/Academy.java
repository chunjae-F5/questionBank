package com.example.f5.user.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
public class Academy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "name", nullable = false)
    @Comment(value = "학원 이름")
    private String name;

    @Column(name = "location", nullable = false)
    @Comment(value = "학원 위치")
    private String location;

    @Column(name = "logo", nullable = false)
    @Comment(value = "학원 로고 이미지")
    private String logo;

    @Column(name = "deleted_yn", nullable = false)
    @Comment(value = "탈퇴여부")
    private boolean deletedYN = false;
}
