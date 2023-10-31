package com.example.f5.exam.controller;

import com.example.f5.exam.dto.ExamArchiveListDTO;
import com.example.f5.exam.entity.Archive;
import com.example.f5.exam.service.ExamArchiveService;
import com.google.gson.Gson;
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
public class ExamArchiveController {

    private final ExamArchiveService examArchiveService;

    public ExamArchiveController(ExamArchiveService examArchiveService) {
        this.examArchiveService = examArchiveService;
    }

    @GetMapping("/exam-archive/list")
    public String getExamArchivePage(Model model, @SessionAttribute(name = "userId", required = false) String userId){

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
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ URLEncoder.encode(pdfName,"UTF-8")+".pdf");

                Resource resource = new FileSystemResource(pdfUrl);

                return ResponseEntity.ok().headers(headers).body(resource);
            }else{
                return ResponseEntity.notFound().build();
            }
    }

    @GetMapping("/exam-archive/preview")
    @ResponseBody
    public ResponseEntity<String> previewPdf(@RequestParam int idx) {
        Optional<Archive> previewPng = examArchiveService.loadArchiveFromDatabase(Long.valueOf(idx));

        String preveiwUrl = previewPng.get().getPreviewImg();
        String path = preveiwUrl.replace("\\", "\\\\");
        String jsonFilePath = "\"" + path + "\"";
//        return ResponseEntity.ok().body(jsonFilePath);

        String remoteUrl = "https://drive.google.com/file/d/1Ktb17suT1Cif0znm7CFKKWDO_9NLRNHI/view?usp=sharing";

        // 파일 식별자 추출
        String fileId = remoteUrl.substring(remoteUrl.indexOf("/file/d/") + 8);
        fileId = fileId.substring(0, fileId.indexOf("/view"));

        // 새로운 다운로드 가능한 URL 생성
        String downloadUrl = "https://drive.google.com/uc?id=" + fileId;

        System.out.println(fileId);

        // JSON 객체를 JSON 문자열로 변환
        Gson gson = new Gson();
        String json = gson.toJson(downloadUrl);

        System.out.println(json);
        return ResponseEntity.ok().body(json);
    }

}
