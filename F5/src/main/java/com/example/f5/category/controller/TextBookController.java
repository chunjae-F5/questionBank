package com.example.f5.category.controller;

import com.example.f5.category.dto.TextBookDto;
import com.example.f5.category.service.TextBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TextBookController {

    private final TextBookService tbService;

    @GetMapping("/")
    public String getTextBookInfo(Model model) {
        List<TextBookDto.ResponseDto> itemList = tbService.getTextBookInfo();
        model.addAttribute("itemList", itemList);
        return "html/main";
    }
}
