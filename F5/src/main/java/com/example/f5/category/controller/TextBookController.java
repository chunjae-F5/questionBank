package com.example.f5.category.controller;

import com.example.f5.category.dto.TextBookDto;
import com.example.f5.category.service.TextBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TextBookController {

    private final TextBookService tbService;

    /*메인 페이지 form*/
    @GetMapping("/")
    public String getTextBookInfo(Model model, @SessionAttribute(name = "userId", required = false) String userId) {
        List<TextBookDto.ResponseDto> itemList = tbService.getTextBookInfo();
        model.addAttribute("itemList", itemList);
        if (userId != null) {
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        return "html/main";
    }
}
