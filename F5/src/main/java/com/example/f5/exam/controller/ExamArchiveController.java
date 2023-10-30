package com.example.f5.exam.controller;

import com.example.f5.exam.dto.ExamArchiveListDTO;
import com.example.f5.exam.service.ExamArchiveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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



}
