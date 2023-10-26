package com.example.f5.temporary.controller;

import com.example.f5.temporary.dto.TemporaryQuestionDto;
import com.example.f5.temporary.service.TemporaryQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TemporaryQuestionController {
    private final TemporaryQuestionService tqService;

    @PostMapping("/temporary/item")
    public void saveQuestions(){
        tqService.saveQuestionService();
    }

    @GetMapping("/temporary/item-list")
    public ResponseEntity<List<TemporaryQuestionDto.SaveDataDto>> getAllQuestionsAsJson() {
        List<TemporaryQuestionDto.SaveDataDto> questionDtos = tqService.getAllQuestions();
        return new ResponseEntity<>(questionDtos, HttpStatus.OK);
    }

}
