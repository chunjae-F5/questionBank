package com.example.f5.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class UserDto {
    @Getter
    @Setter
    public static class RegisterResponseDto {
        @NotBlank(message = "아이디는 필수 입력 항목입니다.")
        @Size(min = 5, max = 20, message = "아이디는 5~20자 이내로 작성해주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9_\\-]+$", message = "아이디는 알파벳, 숫자, _, - 만 허용됩니다.")
        private String userId;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 8, max = 16, message = "비밀번호는 8~16자 이내로 작성해주세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "비밀번호는 최소 하나의 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.")
        private String password;
        private String passwordCheck;

        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        private String email;

        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        @Size(min = 2, max = 50, message = "이름은 2~50자 이내로 작성해주세요.")
        private String username;
        private String academyName;
        private String academyLocate;
        private MultipartFile academyLogo;
    }

    @Getter
    public static class LoginResponseDto {
        private String userId;
        private String password;
    }
}
