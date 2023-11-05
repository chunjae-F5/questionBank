package com.example.f5.exam.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.example.f5.exam.dto.ExamArchiveListDTO;
import com.example.f5.exam.entity.Archive;
import com.example.f5.exam.service.ExamArchiveService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ExamArchiveController {

    private final ExamArchiveService examArchiveService;
    private final AmazonS3 s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;


    @GetMapping("/exam-archive/list")
    public String getExamArchivePage(Model model, @SessionAttribute(name = "userId", required = false) String userId) {

        List<ExamArchiveListDTO> archiveListDTOS = examArchiveService.archiveList(userId);
        model.addAttribute("archiveList", archiveListDTOS);

        if (userId != null) {
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        return "html/examArchive";
    }

    @GetMapping("/exam-archive/download")
    public ResponseEntity<Resource> downloadPdf(@RequestParam int idx) throws UnsupportedEncodingException {
        Optional<Archive> downloadPdf = examArchiveService.loadArchiveFromDatabase(Long.valueOf(idx));

        String pdfName = downloadPdf.get().getName();
        String pdfUrl = downloadPdf.get().getQuestion();

        if (pdfUrl != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(pdfName, "UTF-8") + ".pdf");

            Resource resource = new FileSystemResource(pdfUrl);

            return ResponseEntity.ok().headers(headers).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exam-archive/preview")
    @ResponseBody
    public ResponseEntity<String> previewPng(@RequestParam int idx){
        Optional<Archive> previewPng = examArchiveService.loadArchiveFromDatabase(Long.valueOf(idx));

        String imgName = previewPng.get().getPreviewImg();
//        String url = s3Client.getUrl(bucketName, imgName);
//        String urltext = ""+url;

        System.out.println(imgName);
        return ResponseEntity.ok().body(imgName);
    }


}
