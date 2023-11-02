package com.example.f5.exam.service;

import com.example.f5.exam.dto.ExamSaveListDTO;
import com.example.f5.exam.dto.ExamSaveListRequestDTO;
import com.example.f5.exam.dto.ExamSaveRequestDTO;
import com.example.f5.exam.entity.Archive;
import com.example.f5.exam.entity.Exam;
import com.example.f5.exam.entity.Question;
import com.example.f5.exam.repository.ArchiveSaveRepository;
import com.example.f5.exam.repository.ExamSaveRepository;
import com.example.f5.exam.repository.QuestionSaveRepository;
import com.example.f5.util.FileUrl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.svg.converter.SvgConverter;
import com.itextpdf.svg.processors.ISvgConverterProperties;
import com.itextpdf.svg.processors.impl.SvgConverterProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamSaveService {

    private final QuestionSaveRepository questionSaveRepository;
    private final ExamSaveRepository examSaveRepository;
    private final ArchiveSaveRepository archiveSaveRepository;

    private final Gson gson;

    private String PDF_URL = "pdf_file";

    private String DEST;

    // 문제 테이블 저장
    public void questionSave(ExamSaveRequestDTO dtos) {
        List<Question> questions = new ArrayList<>();

        List<ExamSaveRequestDTO.ProcessedData> processedDataList = dtos.getProcessedData();
        if (processedDataList != null) {
            for (ExamSaveRequestDTO.ProcessedData processedData : processedDataList) {
                Question question = new Question();
                question.setNumber(processedData.getNumber());
                question.setType(processedData.getType());
                question.setForm(processedData.getQuestionFormName());
                question.setLevel(processedData.getDifficultyName());
                question.setQuestionFile(processedData.getQuestionUrl());
                question.setContentFile(processedData.getPassageUrl() != null ? processedData.getPassageUrl() : "");

                questions.add(question);
            }
        }

        questionSaveRepository.saveAll(questions);

    }

    // 시험지 테이블 저장
    public void examSave(ExamSaveRequestDTO dtos) {

        if (dtos != null) {
            Exam exam = new Exam();
            int total = dtos.getChoiceAnswer() + dtos.getShortAnswer();
            exam.setHighest(0);
            exam.setHigh(dtos.getHigh());
            exam.setMedium(dtos.getMedium());
            exam.setLow(dtos.getLow());
            exam.setTotal(total);
            exam.setChoiceAnswer(dtos.getChoiceAnswer());
            exam.setShortAnswer(dtos.getShortAnswer());
            exam.setLongAnswer(0);

            examSaveRepository.save(exam);
        }

    }

    // pdf 생성
    public void generatePdf(ExamSaveRequestDTO requestDTOS, String userId, String username) throws IOException {

        FileUrl fileUrl = new FileUrl();
        DEST = fileUrl.selectUrl() + PDF_URL;

        // DTO에서 지문, 문제 url 각 배열에 담기
        List<String> passageUrls = new ArrayList<>();
        List<String> questionUrls = new ArrayList<>();
        for (int i = 0; i < requestDTOS.getProcessedData().size(); i++) {
            passageUrls.add(requestDTOS.getProcessedData().get(i).getPassageUrl().isEmpty() && requestDTOS.getProcessedData().get(i).getPassageUrl() == null ? "" : requestDTOS.getProcessedData().get(i).getPassageUrl());
            questionUrls.add(requestDTOS.getProcessedData().get(i).getQuestionUrl());
        }

        // DTO에서 필요한 값 변수에 담기
        String examName = requestDTOS.getExamName();
        int totQuestion = requestDTOS.getChoiceAnswer() + requestDTOS.getShortAnswer();
        String userName = username + " 선생님";
        String name = "이름: ";
        String date = String.valueOf(LocalDate.now());
        String topLine = totQuestion + "문제 | " + userName + " | " + name;
        String day = "일일";

        // PDF객체 생성
        String pdfFilePath = setPdfName(DEST, requestDTOS.getExamName(), userId);
        PdfDocument pdf = new PdfDocument(new PdfWriter(pdfFilePath));
        Document document = new Document(pdf);

        Color mainLineColor = new DeviceCmyk(100, 0, 20, 0);
        float mainLineWidth = 5;
        Color subLineColor = new DeviceCmyk(0, 0, 0, 20);
        float subLineWidth = 1;


        // 한글 폰트 처리
        PdfFont font = PdfFontFactory.createFont(DEST + "HYGothic-Medium-Regular.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        // Header
        Header headerHandler = new Header(day, date, examName, topLine, mainLineColor, mainLineWidth, subLineColor, subLineWidth);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);

        // Footer
        Footer footerHandler = new Footer(subLineColor, subLineWidth);
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

        // Body
        for (int i = 0; i < passageUrls.size(); i++) {
            convertSvgToPdf(pdf, document, passageUrls.get(i), questionUrls.get(i));
        }

        document.close();
        System.out.println("pdf create success!");

        String outputImgFile = setPngName();

    }


    // PDF 파일 이름 변경
    private String setPdfName(String dest, String examName, String userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(LocalDate.now().format(formatter));
        String dateParse = String.valueOf(date).replaceAll("-", "");
        String pdfName = dest + "exam_" + userId + "_" + examName + "_" + dateParse;
        String extension = ".pdf";

        return pdfName + extension;
    }

    private String setPngName() {

        FileUrl fileUrl = new FileUrl();
        DEST = fileUrl.selectUrl() + PDF_URL;

        return DEST + "image_" + UUID.randomUUID() + ".png";
    }

    private void convertSvgToPdf(PdfDocument pdf, Document document, String passageSvgUrl, String questionSvgUrl) throws IOException {
        // Create a new page for drawing
        PdfPage page = pdf.addNewPage(PageSize.A4);

        // Get the PdfCanvas for drawing
        PdfCanvas canvas = new PdfCanvas(page);

        if (passageSvgUrl.isEmpty() || passageSvgUrl == null) {
            String adjustedQuestionSvgContent = adjustSvgContent(questionSvgUrl);

            InputStream adjustedQuestionSvgInputStream = new ByteArrayInputStream(adjustedQuestionSvgContent.getBytes(StandardCharsets.UTF_8));

            ISvgConverterProperties questionProperties = new SvgConverterProperties().setBaseUri("");
            SvgConverter.drawOnCanvas(adjustedQuestionSvgInputStream, canvas, questionProperties);

        } else {
            String adjustedPassageSvgContent = adjustSvgContent(passageSvgUrl);
            String adjustedQuestionSvgContent = adjustSvgContent(questionSvgUrl);

            InputStream adjustedPassageSvgInputStream = new ByteArrayInputStream(adjustedPassageSvgContent.getBytes(StandardCharsets.UTF_8));
            InputStream adjustedQuestionSvgInputStream = new ByteArrayInputStream(adjustedQuestionSvgContent.getBytes(StandardCharsets.UTF_8));
            ISvgConverterProperties passageProperties = new SvgConverterProperties().setBaseUri("");
            SvgConverter.drawOnCanvas(adjustedPassageSvgInputStream, canvas, passageProperties);

            ISvgConverterProperties questionProperties = new SvgConverterProperties().setBaseUri("");
            SvgConverter.drawOnCanvas(adjustedQuestionSvgInputStream, canvas, questionProperties);

        }

    }

    private String adjustSvgContent(String svgUrl) throws IOException {
        URL url = new URL(svgUrl);
        InputStream inputStream = url.openStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder svgContent = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            svgContent.append(line);
        }

        inputStream.close();

        PageSize pageSize = PageSize.A4;

        // Calculate the width and height for the SVG based on the page size
        float width = pageSize.getWidth() / 2; // Adjust as needed

        // Replace width and height in the SVG content
        return svgContent.toString()
                .replaceAll("width=\"[0-9.]+\"", "width=\"" + width + "\"");
    }


    // 시험지 저장 페이지 api요청
    public List<ExamSaveListDTO.ItemInfo> examSaveList(ExamSaveListRequestDTO itemIdList) throws IOException, InterruptedException {

        String apiURL = "https://tsherpa.item-factory.com/item-img/item-list";

        String jsonRequestBody = gson.toJson(itemIdList);

        // HttpClient 객체 생성
        HttpClient httpClient = HttpClient.newHttpClient();

        // POST 요청 준비
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .header("Accept", "*/*")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                .build();

        // 요청 보내기
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

            if (responseJson != null && "Y".equals(responseJson.get("successYn").getAsString())) {
                List<ExamSaveListDTO.ItemInfo> itemInfoList = new ArrayList<>();

                if (responseJson.has("itemList")) {
                    for (int i = 0; i < responseJson.get("itemList").getAsJsonArray().size(); i++) {
                        JsonObject itemObject = responseJson.get("itemList").getAsJsonArray().get(i).getAsJsonObject();

                        ExamSaveListDTO.ItemInfo itemInfo = new ExamSaveListDTO.ItemInfo();
                        itemInfo.setItemNo(itemObject.get("itemNo").getAsInt());
                        itemInfo.setItemId(itemObject.get("itemId").getAsInt());
                        itemInfo.setQuestionFormCode(itemObject.get("questionFormCode").getAsString());
                        itemInfo.setQuestionFormName(itemObject.get("questionFormName").getAsString());
                        itemInfo.setDifficultyName(itemObject.get("difficultyName").getAsString());
                        itemInfo.setLargeChapterName(itemObject.get("largeChapterName").getAsString());
                        itemInfo.setMediumChapterName(itemObject.get("mediumChapterName").getAsString());
                        itemInfo.setPassageId(itemObject.has("passageId") && !itemObject.get("passageId").isJsonNull() ? itemObject.get("passageId").getAsInt() : 0);
                        itemInfo.setPassageUrl(itemObject.has("passageUrl") && !itemObject.get("passageUrl").isJsonNull() ? itemObject.get("passageUrl").getAsString() : "");
                        itemInfo.setQuestionUrl(itemObject.get("questionUrl").getAsString());

                        itemInfoList.add(itemInfo);
                    }
                }
                return itemInfoList;
            }
        }

        return Collections.emptyList();
    }

    // 보관함 DB 저장
    public void archiveSave(ExamSaveRequestDTO requestDTOS, String userId) {
//        String userId = "sky";

        FileUrl fileUrl = new FileUrl();
        DEST = fileUrl.selectUrl() + PDF_URL;

        if (requestDTOS != null) {


            Archive archive = new Archive();
            archive.setUserId(userId);
            archive.setFlag("M");
            archive.setGrade("1");
            archive.setName(requestDTOS.getExamName());
            archive.setTotal(requestDTOS.getShortAnswer() + requestDTOS.getChoiceAnswer());
            archive.setQuestion(setPdfName(DEST, requestDTOS.getExamName(), userId));
            archive.setPreviewImg(setPngName());
            archiveSaveRepository.save(archive);
        }

    }
}