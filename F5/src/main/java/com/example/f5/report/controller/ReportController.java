package com.example.f5.report.controller;

import com.example.f5.report.dto.ReportDto;
import com.example.f5.report.service.ReportService;
import com.example.f5.util.FileUpload;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final FileUpload fileUpload;

    @PostMapping("/report")
    public ResponseEntity<String> saveReport(@ModelAttribute ReportDto.ReportRequestDto requestDto, HttpServletRequest request) throws IOException {
        /*파일 업로드*/
        fileUpload.uploadFile(requestDto.getFile(), "ReportImg");
        return reportService.saveReport(requestDto, request);
    }
}
