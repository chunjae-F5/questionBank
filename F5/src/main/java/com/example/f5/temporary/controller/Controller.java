package com.example.f5.temporary.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
@RequestMapping("/hi")
public class Controller {

    @GetMapping("/1")
    public String hi() {

        return "hi";
    }
}
