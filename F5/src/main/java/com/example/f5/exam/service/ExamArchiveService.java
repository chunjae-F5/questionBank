package com.example.f5.exam.service;

import com.example.f5.exam.dto.ExamArchiveListDTO;
import com.example.f5.exam.entity.Archive;
import com.example.f5.exam.repository.ArchiveSaveRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

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

    public Optional<Archive> downloadPdf(Long idx) {
        Optional<Archive> archive = archiveSaveRepository.findById(idx);
        return archive;
    }
}
