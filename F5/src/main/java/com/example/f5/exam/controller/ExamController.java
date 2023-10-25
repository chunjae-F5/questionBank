package com.example.f5.exam.controller;

import com.example.f5.exam.dto.ExamDto;
import com.example.f5.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String createExamForm(@RequestParam int itemId, Model model) {
        List<ExamDto.CategoryResponse> itemList = examService.getCategory(itemId);
        model.addAttribute("itemList", itemList);
        return "html/sub02";
    }

//    @GetMapping("/category/select")
//    public @ResponseBody List<ExamDto.CategoryResponse> createExamForm(@RequestParam int itemId) {
//        List<ExamDto.CategoryResponse> itemList = examService.getCategory(itemId);
//        return itemList;
//    }
}

