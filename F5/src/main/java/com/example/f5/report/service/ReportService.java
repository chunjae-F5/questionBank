package com.example.f5.report.service;

import com.example.f5.report.dto.ReportDto;
import com.example.f5.report.entity.Report;
import com.example.f5.report.repository.ReportRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository repository;

    public ResponseEntity<String> saveReport(ReportDto.ReportRequestDto requestDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 되어있지 않음.");
        }
        String userId = String.valueOf(session.getAttribute("userId"));
        Report report = new Report(requestDto.getType(), requestDto.getContent(), requestDto.getFile().getOriginalFilename(), userId);
        repository.save(report);
        return ResponseEntity.ok("문제 신고 완료");
    }
}
