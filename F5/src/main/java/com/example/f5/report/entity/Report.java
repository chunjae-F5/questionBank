package com.example.f5.report.entity;

import com.example.f5.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "type", nullable = false)
    @Comment(value = "오류 유형")
    private String type;

    @Column(name = "content", nullable = false)
    @Comment(value = "오류 내용")
    private String content;

    @Column(name = "file", nullable = false)
    @Comment(value = "첨부 파일")
    private String file;

    @Column(name = "user_id", nullable = false)
    @Comment(value = "신고자 아이디")
    private String userId;

    @Column(name = "delete_yn", nullable = false)
    @Comment(value = "삭제여부")
    private boolean deletedYN;

    public Report(String type, String content, String file, String userId) {
        this.type = type;
        this.content = content;
        this.file = file;
        this.userId = userId;
    }

    public Report() {

    }
}
