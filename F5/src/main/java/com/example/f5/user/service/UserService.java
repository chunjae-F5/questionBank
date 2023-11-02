package com.example.f5.user.service;

import com.example.f5.user.dto.UserDto;
import com.example.f5.user.entity.Academy;
import com.example.f5.user.entity.User;
import com.example.f5.user.repository.AcademyRepository;
import com.example.f5.user.repository.UserRepository;
import com.example.f5.util.FileUrl;
import com.example.f5.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AcademyRepository academyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    /*회원가입*/
    public ResponseEntity<String> register(UserDto.RegisterResponseDto form) {
        ResponseEntity<String> result = saveUser(form);
        saveAcademy(form);

        return result;
    }

    /*학원 정보 저장*/
    private void saveAcademy(UserDto.RegisterResponseDto form) {
        Academy academy = new Academy();
        academy.setName(form.getAcademyName());
        academy.setLocation(form.getAcademyLocate());
        academy.setLogo(String.valueOf(form.getAcademyLogo()));
        academyRepository.save(academy);
    }

    /*회원 정보 저장*/
    private ResponseEntity<String> saveUser(UserDto.RegisterResponseDto form) {
        User user = new User();

        user.setId(form.getUserId());
        if (!form.getPassword().equals(form.getPasswordCheck())) {
            return ResponseEntity.status(400).body("비밀번호 일치하지 않음");
        }
        user.setPassword(passwordEncoder.sha256Encoding(form.getPassword()));
        user.setName(form.getUsername());
        user.setEmail(form.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    public ResponseEntity<String> login(UserDto.LoginResponseDto form, HttpServletRequest httpServletRequest) {
        User user = userRepository.findById(form.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디로 회원을 찾을 수 없음")
        );
        if (passwordEncoder.passwordCheck(form.getUserId(), form.getPassword())) {
            httpServletRequest.getSession().invalidate();
            HttpSession session = httpServletRequest.getSession(true);

            String userName = user.getName();
            session.setAttribute("userId", form.getUserId());
            session.setAttribute("userName", userName);
            session.setMaxInactiveInterval(1800);
            return ResponseEntity.ok().body("");
        }
        return ResponseEntity.status(401).body("아이디 비밀번호가 일치하지 않음.");
    }

    public boolean checkIdDuplicated(String userId) {
        return userRepository.findById(userId).isPresent();
    }
}
