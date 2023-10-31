package com.example.f5.exam.controller;

import com.example.f5.exam.dto.ExamArchiveListDTO;
import com.example.f5.exam.entity.Archive;
import com.example.f5.exam.service.ExamArchiveService;
import com.example.f5.exam.service.ImageSavingListener;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

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
    public byte[] previewPdf(@RequestParam int idx) throws IOException {
        Optional<Archive> previewPdf = examArchiveService.loadPdfFromDatabase(Long.valueOf(idx));
        String pdfName = previewPdf.get().getName();
        String pdfUrl = previewPdf.get().getQuestion();
        System.out.println(pdfUrl);

        try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdfUrl))) {
            IEventListener listener = new ImageSavingListener();
            PdfCanvasProcessor processor = new PdfCanvasProcessor(listener);
            for (int page = 1; page <= pdfDocument.getNumberOfPages(); page++) {
                processor.processPageContent(pdfDocument.getPage(page));
            }
        }

        // Assuming that the image has been saved to a ByteArrayOutputStream
        // Replace this with the actual ByteArrayOutputStream containing the image data
        ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();

        return imageOutputStream.toByteArray();

    }

}
