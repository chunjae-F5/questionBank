package com.example.f5.exam.controller;

import com.example.f5.exam.dto.ExamDto;
import com.example.f5.exam.service.ExamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/item-img/chapter/item-list")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getItemList(@RequestBody ExamDto.itemInfoRequest requestDto, HttpSession session) {
        List<ExamDto.itemInfoResponse> itemList = examService.getItemList(requestDto);
//        session.setAttribute("subjectId", requestDto. getMinorClassification().get(0));
        session.setAttribute("itemList", itemList);
        Map<String, String> res = new HashMap<>();
        res.put("url", "/item-img/chapter/item-list/red");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/item-img/chapter/item-list/red")
    public String red(HttpSession session,Model model) {
        List<ExamDto.itemInfoResponse> itemList = (List<ExamDto.itemInfoResponse>)session.getAttribute("itemList");
//        List<ExamDto.itemInfoResponse> subjectId = (List<ExamDto.itemInfoResponse>)session.getAttribute("subjectId");
        model.addAttribute("itemList", itemList);
//        model.addAttribute("subjectId", subjectId);

        if (itemList.get(0).getPassageUrl() != null) {
        return "html/sub03_01_01";
        }
            return "html/sub03_01";
    }

    /*시험 제작 페이지 form*/
    @GetMapping("/category/select")
    public String createExamForm(@RequestParam int itemId, Model model) throws JsonProcessingException {
        ExamDto.Curriculum itemList = examService.getCategory(itemId);
        List<String> evaluationList = examService.getEvaluation(itemId);
        model.addAttribute("itemList", itemList);
        model.addAttribute("evaluationList", evaluationList);
        return "html/sub02";
    }
}

