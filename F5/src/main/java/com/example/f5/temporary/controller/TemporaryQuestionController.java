package com.example.f5.temporary.controller;

import com.example.f5.temporary.service.TemporaryQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TemporaryQuestionController {
    private final TemporaryQuestionService tqService;

    @PostMapping("/temporary/item")
    public void saveQuestions(){
        tqService.saveQuestionService();
    }
}
