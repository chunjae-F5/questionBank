package com.example.f5.util;

import com.example.f5.user.entity.User;
import com.example.f5.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class PasswordEncoder {

    private final UserRepository userRepository;

    public String sha256Encoding(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("비밀번호 암호화 중 에러 발생");
        }
    }

    public boolean passwordCheck(String userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디로 회원을 찾을 수 없음")
        );
        return user.getPassword().equals(sha256Encoding(password));
    }
}
