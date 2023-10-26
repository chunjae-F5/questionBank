package com.example.f5.exam.controller;

import com.example.f5.exam.dto.ExamDto;
import com.example.f5.exam.service.ExamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/item-img/chapter/item-list")
    public String getItemList(@RequestBody ExamDto.itemInfoRequest requestDto, Model model) {
        List<ExamDto.itemInfoResponse> itemList = examService.getItemList(requestDto);
        model.addAttribute("itemList", itemList);

        return "html/sub03_01";
    }

    @GetMapping("/category/select")
    public String createExamForm(@RequestParam int itemId, Model model) throws JsonProcessingException {
        ExamDto.Curriculum itemList = examService.getCategory(itemId);
        List<String> evaluationList = examService.getEvaluation(itemId);
        model.addAttribute("itemList", itemList);
        model.addAttribute("evaluationList", evaluationList);
        return "html/sub02";
    }
}

