package com.example.f5.exam.controller;

import com.example.f5.exam.dto.ExamArchiveListDTO;
import com.example.f5.exam.entity.Archive;
import com.example.f5.exam.service.ExamArchiveService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getExamArchivePage(Model model){

        List<ExamArchiveListDTO> archiveListDTOS = examArchiveService.archiveList();
        model.addAttribute("archiveList", archiveListDTOS);

        return "html/examArchive";
    }

    @GetMapping("/exam-archive/download")
    public ResponseEntity<Resource> downloadPdf(@RequestParam int idx) throws UnsupportedEncodingException {
        Optional<Archive> downloadPdf = examArchiveService.loadPdfFromDatabase(Long.valueOf(idx));

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
    public ResponseEntity<byte[]> previewPdf(@RequestParam int idx) throws IOException {
//        Optional<Archive> previewPdf = examArchiveService.loadPdfFromDatabase(Long.valueOf(idx));
//        String pdfName = previewPdf.get().getName();
//        String pdfUrl = previewPdf.get().getQuestion();
//        System.out.println(pdfUrl);

        String imagePath = examArchiveService.convertPdfToImage(Long.valueOf(idx));

        File file = new File(imagePath);
        Resource resource = new FileSystemResource(file); // 정적 리소스에서 파일 로드
        byte[] pdfBytes = resource.getInputStream().readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(imagePath, imagePath);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

//        return imagePath;

    }

}
