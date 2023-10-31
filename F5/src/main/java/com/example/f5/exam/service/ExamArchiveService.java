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
public class ExamArchiveService {

    private final ArchiveSaveRepository archiveSaveRepository;
    public static final String DEST = "D:\\pdf_file\\";

    public ExamArchiveService(ArchiveSaveRepository archiveSaveRepository) {
        this.archiveSaveRepository = archiveSaveRepository;
    }

    public List<ExamArchiveListDTO> archiveList(String userId) {

        List<ExamArchiveListDTO> archiveListDTOS= archiveSaveRepository.findByUserId(userId);

        return archiveListDTOS;
    }

    public Optional<Archive> loadArchiveFromDatabase(Long idx) {
        Optional<Archive> archive = archiveSaveRepository.findById(idx);
        return archive;
    }

//    public String convertPdfToImage(Long idx) throws IOException {
//        Optional<Archive> archive = loadPdfFromDatabase(idx);
//        String pdfName = archive.get().getName();
//        String pdfUrl = archive.get().getQuestion();
//        System.out.println(pdfUrl);
//
//        try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdfUrl), new PdfWriter(pdfName))) {
////            IEventListener listener = new ImageSavingListener(pdfUrl);
//            IEventListener listener = new ImageSavingListener();
//            PdfCanvasProcessor processor = new PdfCanvasProcessor(listener);
//            for (int page = 1; page <= pdfDocument.getNumberOfPages(); page++) {
//                processor.processPageContent(pdfDocument.getPage(page));
//            }
//        }
//
//        // 이미지가 저장된 경로를 반환합니다.
//        return pdfUrl;
//    }
//
//    private static class ImageSavingListener implements IEventListener {
////        private String pdfUrl;
////        public ImageSavingListener(String pdfUrl) {
////            this.pdfUrl = pdfUrl;
////        }
//
//        @Override
//        public void eventOccurred(IEventData data, EventType type) {
//            if (type == EventType.RENDER_IMAGE) {
//                ImageRenderInfo imageInfo = (ImageRenderInfo) data;
//                PdfImageXObject imageObject = imageInfo.getImage();
//                try {
//                    // 이미지를 파일로 저장하는 로직
//                    String imageFileName = "image_" + UUID.randomUUID() + ".jpg";
//                    String imagePath = Paths.get(DEST, imageFileName).toString();
//
//                    File imageFile = new File(imagePath);
//                    try (FileOutputStream fos = new FileOutputStream(imageFile)) {
//                        fos.write(imageObject.getImageBytes());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        @Override
//        public Set<EventType> getSupportedEvents() {
//            return null;
//        }
//
//    }

}
