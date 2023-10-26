package com.example.f5.temporary.controller;

import com.example.f5.temporary.dto.SimilarQueDto;
import com.example.f5.temporary.entity.DeletedQuestion;
import com.example.f5.temporary.service.QuestionService;
import com.example.f5.temporary.service.SimilarObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/030201")
    public String queMain() {
        return "html/sub03_02_01";
    }

    @GetMapping("/sim")
    public String simMain() {
        return "html/sub03_02_02";
    }

    @PostMapping("/similarQue")
    @ResponseBody
    public List<SimilarObject> similarQue(@RequestBody SimilarQueDto.Request requestDto) {
        return questionService.similarQuestion(requestDto.getItems());
    }

    @GetMapping("/deleteQue")
    public String deleteQue() {
        return "html/sub03_03";
    }

    @PostMapping("/deleteQue")
    public String deleteQue(@RequestBody DeletedQuestion request) {
        return questionService.deleteQuestion(request);
    }

    @GetMapping("/upDown")
    public String upDown() {
        return "html/sub03_01";
    }
}


