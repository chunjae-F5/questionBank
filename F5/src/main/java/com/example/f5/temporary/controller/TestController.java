package com.example.f5.temporary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("as")
    public String as(){
        return "html/error";
    }
}
