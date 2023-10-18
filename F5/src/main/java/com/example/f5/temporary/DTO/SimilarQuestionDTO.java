package com.example.f5.temporary.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Slf4j
public class SimilarQuestionDTO {

    private int idx;

    private int number;

    private String form;

    private String level;  // 난이도 -> quewstionLevelName

    private String questionFile;  // json

}
