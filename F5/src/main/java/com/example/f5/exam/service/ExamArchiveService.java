package com.example.f5.exam.service;

import com.example.f5.exam.dto.ExamArchiveListDTO;
import com.example.f5.exam.entity.Archive;
import com.example.f5.exam.repository.ArchiveSaveRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamArchiveService {

    private final ArchiveSaveRepository archiveSaveRepository;
    private String PDF_URL = "pdf_file";

    private String DEST;
    @Value("${windows.file.pdfDir}")
    private String widowsFileDir;

    @Value("${linux.file.pdfDir}")
    private String linuxFileDir;


    public List<ExamArchiveListDTO> archiveList(String userId) {

        List<ExamArchiveListDTO> archiveListDTOS = archiveSaveRepository.findByUserId(userId);

        return archiveListDTOS;
    }

    public Optional<Archive> loadArchiveFromDatabase(Long idx) {
        Optional<Archive> archive = archiveSaveRepository.findById(idx);
        return archive;
    }

}
