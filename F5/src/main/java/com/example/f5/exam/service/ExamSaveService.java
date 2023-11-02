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
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.svg.converter.SvgConverter;
import com.itextpdf.svg.processors.ISvgConverterProperties;
import com.itextpdf.svg.processors.impl.SvgConverterProperties;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static com.itextpdf.kernel.geom.PageSize.A4;


@Service
@RequiredArgsConstructor
public class ExamSaveService {

    private final QuestionSaveRepository questionSaveRepository;
    private final ExamSaveRepository examSaveRepository;
    private final ArchiveSaveRepository archiveSaveRepository;

    private final Gson gson;

    private String PDF_URL = "pdf_file";

    private String DEST;
    @Value("${windows.file.pdfDir}")
    private String widowsFileDir;

    @Value("${linux.file.pdfDir}")
    private String linuxFileDir;

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
        DEST = fileUrl.selectUrl(widowsFileDir, linuxFileDir) + PDF_URL;
        if (DEST.contains("C:")) {
            DEST = DEST + "\\";

        } else {
            DEST = DEST + "/";
        }

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
        String subjectName = requestDTOS.getSubjectName();

        // PDF객체 생성
        String pdfFilePath = setPdfName(DEST, requestDTOS.getExamName(), userId);
        PdfDocument pdf = new PdfDocument(new PdfWriter(pdfFilePath));
        Document document = new Document(pdf);

        Color mainLineColor = new DeviceCmyk(100, 0, 20, 0);
        float mainLineWidth = 5;
        Color subLineColor = new DeviceCmyk(0, 0, 0, 20);
        float subLineWidth = 1;

        Color fontGray = new DeviceCmyk(0, 0, 0, 80);

        // 한글 폰트 처리
        PdfFont font = PdfFontFactory.createFont(DEST + "HYGothic-Medium-Regular.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_NOT_EMBEDDED);

        // Header
        Header headerHandler = new Header(day, date, examName, topLine, mainLineColor, mainLineWidth, subLineColor, subLineWidth);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);

        // Footer
        Footer footerHandler = new Footer(subLineColor, subLineWidth);
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

        // Body
        for (int i = 0; i < questionUrls.size(); i++) {
            int number = requestDTOS.getProcessedData().get(i).getNumber();
            String level = requestDTOS.getProcessedData().get(i).getDifficultyName();
            String type = requestDTOS.getProcessedData().get(i).getQuestionFormName();

            System.out.println("number : " + number);

            if(passageUrls.get(i).isEmpty() || passageUrls.get(i) == null){
                convertSvgToPdf(pdf, document, questionUrls.get(i), i, number, font, level, type, mainLineColor, fontGray);
            }else{
                convertSvgToPassagePdf(pdf, document, passageUrls.get(i), questionUrls.get(i), i, number, font, level, type, mainLineColor, fontGray);
            }

        }

        document.close();
        System.out.println("pdf create success!");

        String outputImgFile = setPngName();

        PDDocument doc = PDDocument.load(new File(pdfFilePath));
        PDFRenderer pdfRenderer = new PDFRenderer(doc);

        // 첫 번째 페이지를 이미지로 렌더링
        PDPage firstPage = doc.getPage(0);
        BufferedImage image = pdfRenderer.renderImage(0);

        // 이미지를 파일로 저장
        ImageIO.write(image, "PNG", new File(outputImgFile));

        doc.close();
        System.out.println("Image saved to " + outputImgFile);

    }

    // 텍스트 추가 메서드
    private static void addTextToPdf(Document doc, String text, PdfFont font, Color color, float x, float y, int fontSize) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFont(font).setFontColor(color);
        paragraph.setFontSize(fontSize); // 글자 크기 조정
        paragraph.setMargin(0); // 여백 제거
        doc.showTextAligned(paragraph, x, y, TextAlignment.LEFT);
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
        DEST = fileUrl.selectUrl(widowsFileDir, linuxFileDir) + PDF_URL;
        if (DEST.contains("C:")) {
            DEST = DEST + "\\";

        } else {
            DEST = DEST + "/";
        }

        return DEST + "image_" + UUID.randomUUID() + ".png";
    }

    private void convertSvgToPdf(PdfDocument pdf, Document document, String questionSvgUrl, int i, int number, PdfFont font, String level, String type, Color mainLineColor, Color fontGray) throws IOException {
        PdfPage page;
        if (i % 2 == 0) {
            page = pdf.addNewPage(A4);
        }else{
            page = pdf.getLastPage();
        }
        // A4 사이즈의 새로운 페이지 생성
//        PdfPage page = pdf.addNewPage(PageSize.A4);

        // 페이지의 회전 설정 (Portrait 또는 Landscape)
//        page.setRotation(0);

        // PDF에 그림을 그리기 위한 PdfCanvas 가져오기
        PdfCanvas canvas = new PdfCanvas(page);

        // 왼쪽 이미지 배치 위치 좌표
        float leftX = 30;
        float leftY = 230;

        // 오른쪽 이미지 배치 위치 좌표
        float rightX = 327.5F;
        float rightY = 230;

        // 이미지 너비와 높이 설정 (필요에 따라 조정)
//        float imageWidth = 200f;
//        float imageHeight = 150f;

        // 질문 이미지에 대한 SVG 콘텐츠를 조정
        String adjustedQuestionSvgContent = adjustSvgContent(questionSvgUrl);

        // 조정된 질문 이미지를 InputStream으로 변환
        InputStream adjustedQuestionSvgInputStream = new ByteArrayInputStream(adjustedQuestionSvgContent.getBytes(StandardCharsets.UTF_8));

        // 질문 이미지를 PDF 캔버스에 그리기
        ISvgConverterProperties questionProperties = new SvgConverterProperties().setBaseUri("");
        if (i % 2 == 0) {
            addTextToPdf(document, "0"+number, font, mainLineColor, leftX, leftY+370, 14);
            addTextToPdf(document, level, font, fontGray, leftX + 30, leftY+370, 10);
            addTextToPdf(document, type, font, fontGray, leftX + 50, leftY+370, 10);

            SvgConverter.drawOnCanvas(adjustedQuestionSvgInputStream, canvas, leftX, leftY, questionProperties);
        } else {
            addTextToPdf(document, "0"+number, font, mainLineColor, rightX, rightY+370, 14);
            addTextToPdf(document, level, font, fontGray, rightX + 30, rightY+370, 10);
            addTextToPdf(document, type, font, fontGray, rightX + 50, rightY+370, 10);

            SvgConverter.drawOnCanvas(adjustedQuestionSvgInputStream, canvas, rightX, rightY, questionProperties);
        }

    }


    private void convertSvgToPassagePdf(PdfDocument pdf, Document document, String passageSvgUrl, String questionSvgUrl, int i, int number, PdfFont font, String level, String type, Color mainLineColor, Color fontGray) throws IOException {
        // Create a new A4-sized page
//        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        PdfPage page;
        if (i % 2 == 0) {
            page = pdf.addNewPage(A4);
        }else{
            page = pdf.getLastPage();
        }

        // A4 사이즈의 새로운 페이지 생성
//        PdfPage page = pdf.addNewPage(PageSize.A4);

        // 페이지의 회전 설정 (Portrait 또는 Landscape)
        page.setRotation(0);

        // PDF에 그림을 그리기 위한 PdfCanvas 가져오기
        PdfCanvas canvas = new PdfCanvas(page);

        // 왼쪽 이미지 배치 위치 좌표
        float leftX = 30;
        float leftY = 230;

        // 오른쪽 이미지 배치 위치 좌표
        float rightX = 327.5F;
        float rightY = 230;

//        float imageYY = 300;

        // 이미지 너비와 높이 설정 (필요에 따라 조정)
//        float imageWidth = 200f;
//        float imageHeight = 150f;


        // 본문 이미지와 질문 이미지의 SVG 콘텐츠 조정
        String adjustedPassageSvgContent = adjustSvgContent(passageSvgUrl);
        String adjustedQuestionSvgContent = adjustSvgContent(questionSvgUrl);

        // 조정된 본문 이미지와 질문 이미지를 InputStream으로 변환
        InputStream adjustedPassageSvgInputStream = new ByteArrayInputStream(adjustedPassageSvgContent.getBytes(StandardCharsets.UTF_8));
        InputStream adjustedQuestionSvgInputStream = new ByteArrayInputStream(adjustedQuestionSvgContent.getBytes(StandardCharsets.UTF_8));

        ISvgConverterProperties passageProperties = new SvgConverterProperties().setBaseUri("");
        if( i % 2 == 0) {
            // 본문 이미지를 PDF 캔버스에 그리기
            SvgConverter.drawOnCanvas(adjustedPassageSvgInputStream, canvas, leftX, leftY, passageProperties);

            addTextToPdf(document, "0"+number, font, mainLineColor, leftX, leftY+370, 14);
            addTextToPdf(document, level, font, fontGray, leftX + 30, leftY+370, 10);
            addTextToPdf(document, type, font, fontGray, leftX + 50, leftY+370, 10);
            // 질문 이미지를 PDF 캔버스에 그리기
            float leftYY = leftY - 200;
            ISvgConverterProperties questionProperties = new SvgConverterProperties().setBaseUri("");
            SvgConverter.drawOnCanvas(adjustedQuestionSvgInputStream, canvas, leftX, leftYY, questionProperties);
        }else{
            // 본문 이미지를 PDF 캔버스에 그리기
            SvgConverter.drawOnCanvas(adjustedPassageSvgInputStream, canvas, rightX, rightY, passageProperties);

            addTextToPdf(document, "0"+number, font, mainLineColor, rightX, rightY+370, 14);
            addTextToPdf(document, level, font, fontGray, rightX + 30, rightY+370, 10);
            addTextToPdf(document, type, font, fontGray, rightX + 50, rightY+370, 10);
            // 질문 이미지를 PDF 캔버스에 그리기
            float rightYY = rightY - 200;
            ISvgConverterProperties questionProperties = new SvgConverterProperties().setBaseUri("");
            SvgConverter.drawOnCanvas(adjustedQuestionSvgInputStream, canvas, rightX, rightYY, questionProperties);
        }

    }

    private String adjustSvgContent(String svgUrl) throws IOException {
        // SVG 이미지 URL에서 InputStream을 열기
        URL url = new URL(svgUrl);
        InputStream inputStream = url.openStream();

        // SVG 콘텐츠를 읽기 위한 BufferedReader 생성
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder svgContent = new StringBuilder();
        String line;

        // SVG 콘텐츠를 읽어서 문자열로 저장
        while ((line = reader.readLine()) != null) {
            svgContent.append(line);
        }
        // InputStream 닫기
        inputStream.close();

        // SVG 콘텐츠에서 너비와 높이 조정 (필요한대로)
        PageSize pageSize = A4;
        float width = pageSize.getWidth() / 2;
        float height = pageSize.getHeight() - 250;

        // SVG 콘텐츠의 너비 속성 조정
        return svgContent.toString()
                .replaceAll("width=\"[0-9.]+\"", "width=\"" + width + "\"")
                .replaceAll("height=\"[0-9.]+\"", "height=\"" + height + "\"");

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
        DEST = fileUrl.selectUrl(widowsFileDir, linuxFileDir) + PDF_URL;
        if (DEST.contains("C:")) {
            DEST = DEST + "\\";

        } else {
            DEST = DEST + "/";
        }

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