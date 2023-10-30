package com.example.f5.exam.service;

import com.example.f5.exam.dto.ExamArchiveListDTO;
import com.example.f5.exam.repository.ArchiveSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamArchiveService {

    private final ArchiveSaveRepository archiveSaveRepository;

    public ExamArchiveService(ArchiveSaveRepository archiveSaveRepository) {
        this.archiveSaveRepository = archiveSaveRepository;
    }

    public List<ExamArchiveListDTO> archiveList() {
        String userId = "sky";

        List<ExamArchiveListDTO> archiveListDTOS= archiveSaveRepository.findByUserId(userId);

        return archiveListDTOS;
    }

}
