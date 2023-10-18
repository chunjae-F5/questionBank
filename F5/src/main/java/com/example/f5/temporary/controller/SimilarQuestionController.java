package com.example.f5.temporary.controller;

import com.example.f5.temporary.service.TemporaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exam")
public class SimilarQuestionController {

    private final TemporaryService temporaryService;

    public SimilarQuestionController(TemporaryService temporaryService) {
        this.temporaryService = temporaryService;
    }

    @GetMapping("")
    public String exam(Model model) {

        return "html/sub03_02_01";
    }
}
