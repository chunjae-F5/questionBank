package com.example.f5.user.service;

import com.example.f5.user.entity.User;
import com.example.f5.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    public User registerNewUser(User user) {
        // 비밀번호를 해싱하여 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
