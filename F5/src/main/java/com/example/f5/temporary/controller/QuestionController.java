package com.example.f5.temporary.controller;

import com.example.f5.temporary.dto.SimilarQueDto;
import com.example.f5.temporary.entity.DeletedQuestion;
import com.example.f5.temporary.service.QuestionService;
import com.example.f5.temporary.service.SimilarObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    @GetMapping("/sendData")
    public String sendData(@RequestParam("itemId") String itemId, @RequestParam("count") String count, Model model) {
        model.addAttribute("itemId", itemId);
        model.addAttribute("count", count);

        return "html/sub03_02_02";
    }

    @GetMapping("/sendDataForSave")
    public String sendDataForSave(@RequestParam("itemIds") String encodedItemIds, Model model) {
        String decodedItemIds = URLDecoder.decode(encodedItemIds, StandardCharsets.UTF_8);

        List<String> itemIds = new ArrayList<>();
        try {
            itemIds = new ObjectMapper().readValue(decodedItemIds, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            // 예외 처리
        }

        model.addAttribute("itemIds", itemIds);

        return "html/sub03_03";
    }

}


