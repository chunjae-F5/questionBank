package com.example.f5.exam.controller;

import com.example.f5.exam.dto.ExamSaveListDTO;
import com.example.f5.exam.dto.ExamSaveListRequestDTO;
import com.example.f5.exam.dto.ExamSaveRequestDTO;
import com.example.f5.exam.service.ExamSaveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExamSaveController {

    private final ExamSaveService examSaveService;
    private final ObjectMapper objectMapper;

    // 시험지 저장 페이지
    @GetMapping("/exam-save/list")
    public String getExamSavePage(){
        return "html/sub04_01";
    }

    // 문제 api 가져오기
    @PostMapping(value = "/exam-save/list")
    @ResponseBody
    public List<ExamSaveListDTO.ItemInfo> getExamSaveList(@RequestBody ExamSaveListRequestDTO itemIdList) throws IOException, InterruptedException {

        if (itemIdList == null) {
            return (List<ExamSaveListDTO.ItemInfo>) new IllegalArgumentException("error");
        }
        List<ExamSaveListDTO.ItemInfo> responseDTO = examSaveService.examSaveList(itemIdList);

        return responseDTO;
    }

    // 문제 저장 완료 페이지
    @GetMapping("/exam-save/success")
    public String examSaveSuccess(){
        return "html/sub04_02";
    }

    // 문제 DB 저장
    @PostMapping(value = "/exam-save/save")
    @ResponseBody
    public ResponseEntity<String> examSave(@RequestBody ExamSaveRequestDTO requestDTOS) {

        if(requestDTOS != null){
            examSaveService.questionSave(requestDTOS);
            examSaveService.examSave(requestDTOS);
            try {
                examSaveService.generatePdf(requestDTOS);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }  catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}