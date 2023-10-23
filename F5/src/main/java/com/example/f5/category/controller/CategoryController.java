package com.example.f5.category.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @GetMapping("/select")
    public String category(Model model){


        return "html/sub02";
    }

    @GetMapping("/select1")
    public String category1(Model model){


        return "html/testTree";
    }
}
