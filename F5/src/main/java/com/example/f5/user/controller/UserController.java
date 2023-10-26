package com.example.f5.user.controller;

import com.example.f5.user.entity.User;
import com.example.f5.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("/register")
    public String showResistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "/html/registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.registerNewUser(user);
        return "redirection:/login";
    }

}
