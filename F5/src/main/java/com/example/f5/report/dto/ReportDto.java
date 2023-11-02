package com.example.f5.report.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class ReportDto {
    @Setter
    @Getter
    public static class ReportRequestDto {
        private String type;
        private String content;
        private MultipartFile file;
    }
}
