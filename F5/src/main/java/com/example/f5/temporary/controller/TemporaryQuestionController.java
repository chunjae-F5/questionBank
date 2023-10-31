package com.example.f5.temporary.controller;

import com.example.f5.temporary.dto.TemporaryQuestionDto;
<<<<<<< HEAD
import com.example.f5.temporary.entity.TemporaryQuestion;
import com.example.f5.temporary.repository.TemporaryQuestionRepository;
import com.example.f5.temporary.service.TemporaryQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
=======
import com.example.f5.temporary.service.TemporaryQuestionService;
import lombok.RequiredArgsConstructor;
>>>>>>> 8866b4b84c01b78086f8730b6cd4037785f9532d
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
