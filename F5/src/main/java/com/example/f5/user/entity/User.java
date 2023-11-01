package com.example.f5.user.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "id", nullable = false, unique = true)
    @Comment(value = "아이디")
    private String id;

    @Column(name = "password", nullable = false)
    @Comment(value = "비밀번호")
    private String password;

    @Column(name = "name", nullable = false)
    @Comment(value = "이름")
    private String name;

    @Column(name = "email", nullable = false)
    @Comment(value = "이메일 주소")
    private String email;

    @Column(name = "delete_yn", nullable = false)
    @Comment(value = "탈퇴여부")
    private boolean deletedYN = false;
}
