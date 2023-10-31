package com.example.f5.user.controller;

import ch.qos.logback.core.model.Model;
import com.example.f5.user.dto.UserDto;
import com.example.f5.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*로그인 폼*/
    @GetMapping("/login")
    public String loginForm() {
        return "html/login";
    }

    /*회원가입 폼*/
    @GetMapping("/register")
    public String registerForm() {
        return "html/register";
    }

    /*회원가입 기능*/
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @ModelAttribute UserDto.RegisterResponseDto form) throws IOException {
        System.out.println(form.getAcademyLogo());
        /*파일 업로드*/
        userService.uploadFile(form.getAcademyLogo());
        /*회원가입*/
        return userService.register(form);
    }

    /*로그인 기능*/
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody UserDto.LoginResponseDto form, HttpServletRequest httpServletRequest) {
        return userService.login(form, httpServletRequest);
    }

    /*id 중복 체크*/
    @PostMapping("/check-id-duplication")
    public ResponseEntity<Map<String, Boolean>> checkIdDuplication(@RequestParam String userId) {
        boolean isDuplicated = userService.checkIdDuplicated(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicated", isDuplicated);

        return ResponseEntity.ok(response);
    }

    /*로그아웃*/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
