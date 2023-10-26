package com.example.f5.exam.service;

import com.example.f5.exam.dto.ExamSaveListDTO;
import com.example.f5.exam.dto.ExamSaveListRequestDTO;
import com.example.f5.exam.dto.ExamSaveRequestDTO;
import com.example.f5.exam.entity.Exam;
import com.example.f5.exam.entity.Question;
import com.example.f5.exam.repository.ExamSaveRepository;
import com.example.f5.exam.repository.QuestionSaveRepository;
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
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.properties.AreaBreakType;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExamSaveService {

    private final QuestionSaveRepository questionSaveRepository;
    private final ExamSaveRepository examSaveRepository;

    private final Gson gson;

    public static final String DEST = "D:\\pdf_file\\exam_file.pdf";

    // 문제 테이블 저장
    public void questionSave(ExamSaveRequestDTO dtos) {
        List<Question> questions = new ArrayList<>();

            List<ExamSaveRequestDTO.ProcessedData> processedDataList = dtos.getProcessedData();
            if(processedDataList != null){
                for(ExamSaveRequestDTO.ProcessedData processedData : processedDataList) {
                    Question question = new Question();
                    question.setNumber(processedData.getNumber());
                    question.setType(processedData.getType());
                    question.setForm(processedData.getQuestionFormName());
                    question.setLevel(processedData.getDifficultyName());
                    question.setQuestionFile(processedData.getQuestionUrl());
                    question.setContentFile(processedData.getPassageUrl());

                    questions.add(question);
                }
        }

        questionSaveRepository.saveAll(questions);

    }

    // 시험지 테이블 저장
    public void examSave(ExamSaveRequestDTO dtos) {

        if(dtos != null){
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
    public void generatePdf(ExamSaveRequestDTO requestDTOS) throws IOException {

        // DTO에서 지문, 문제 url 각 배열에 담기
        List<String> passageUrls = new ArrayList<>();
        List<String> questionUrls = new ArrayList<>();
        for(int i = 0; i < requestDTOS.getProcessedData().size(); i++){
            passageUrls.add(requestDTOS.getProcessedData().get(i).getPassageUrl());
            questionUrls.add(requestDTOS.getProcessedData().get(i).getQuestionUrl());
        }

        // DTO에서 필요한 값 변수에 담기
        String examName = requestDTOS.getExamName();
        int totQuestion = requestDTOS.getChoiceAnswer() + requestDTOS.getShortAnswer();
        String userName = "뫄뫄선생님";
        String name = "이름: ";
        String date = String.valueOf(LocalDate.now());
        String topLine = totQuestion + "문제 | " + userName + " | " + name;
        String day = "일일";

        // PDF객체 생성
        PdfDocument pdf = new PdfDocument(new PdfWriter(DEST));
        Document document = new Document(pdf);

        Color mainLineColor = new DeviceCmyk(100, 0, 20, 0);
        float mainLineWidth = 5;
        Color subLineColor = new DeviceCmyk(0, 0, 0, 20);
        float subLineWidth = 1;

        // 한글 폰트 처리
        PdfFont font = PdfFontFactory.createFont("D:/pdf_file/HYGothic-Medium-Regular.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        // Header
        Header headerHandler = new Header(day, date, examName, topLine, mainLineColor, mainLineWidth, subLineColor, subLineWidth);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);

        // Footer
        Footer footerHandler = new Footer(subLineColor, subLineWidth);
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

        // Body
        for(int i = 0; i< passageUrls.size(); i++){
            convertSvgToPdf(pdf, document, passageUrls.get(i), questionUrls.get(i));
        }

        document.close();

        System.out.println("pdf create success!");
    }

    private void convertSvgToPdf(PdfDocument pdf, Document document, String passageSvgUrl, String questionSvgUrl) throws IOException {
        // Create a new A4-sized page
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        // Create a new page for drawing
        PdfPage page = pdf.addNewPage(PageSize.A4);

        // Set page rotation (Portrait or Landscape)
//        page.setRotation(90);

        // Get the PdfCanvas for drawing
        PdfCanvas canvas = new PdfCanvas(page);

        // Adjust the SVG content as needed (e.g., width and height)
        String adjustedPassageSvgContent = adjustSvgContent(passageSvgUrl);
        String adjustedQuestionSvgContent = adjustSvgContent(questionSvgUrl);

        // Draw the adjusted SVG content on the page
        InputStream adjustedPassageSvgInputStream = new ByteArrayInputStream(adjustedPassageSvgContent.getBytes(StandardCharsets.UTF_8));
        InputStream adjustedQuestionSvgInputStream = new ByteArrayInputStream(adjustedQuestionSvgContent.getBytes(StandardCharsets.UTF_8));
        ISvgConverterProperties passageProperties = new SvgConverterProperties().setBaseUri("");
        SvgConverter.drawOnCanvas(adjustedPassageSvgInputStream, canvas, passageProperties);

        ISvgConverterProperties questionProperties = new SvgConverterProperties().setBaseUri("");
        SvgConverter.drawOnCanvas(adjustedQuestionSvgInputStream, canvas, questionProperties);

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

        // Adjust the width and height in the SVG content as needed
        return svgContent.toString()
                .replaceFirst("width=\"\\d+\"", "width=\"300\"")
                .replaceFirst("height=\"\\s+\"", "height=\"auto\"");
    }

//    private void convertSvgToPdf(PdfDocument pdf, Document document, String passageSvgUrl, String questionSvgUrl, int pageNumber) throws IOException {
//        URL passageUrl = new URL(passageSvgUrl);
//        InputStream passageSvgIs = passageUrl.openStream();
//        URL questionUrl = new URL(questionSvgUrl);
//        InputStream questionSvgIs = questionUrl.openStream();
//
//        BufferedReader passageReader = new BufferedReader(new InputStreamReader(passageSvgIs, StandardCharsets.UTF_8));
//        BufferedReader questionReader = new BufferedReader(new InputStreamReader(questionSvgIs, StandardCharsets.UTF_8));
//
//        StringBuilder passageSvgContent = new StringBuilder();
//        StringBuilder questionSvgContent = new StringBuilder();
//
//        String pLine;
//        while ((pLine = passageReader.readLine()) != null) {
//            passageSvgContent.append(pLine);
//        }
//        passageSvgIs.close();
//
//        String qLine;
//        while ((qLine = questionReader.readLine()) != null) {
//            questionSvgContent.append(qLine);
//        }
//        questionSvgIs.close();
//
//        String adjustedPassageSvgContent = passageSvgContent.toString()
//                .replaceFirst("width=\"\\d+\"", "width=\"200\"")
//                .replaceFirst("height=\"\\d+\"", "height=\"auto\"");
//
//        String adjustedQuestionSvgContent = questionSvgContent.toString()
//                .replaceFirst("width=\"\\d+\"", "width=\"200\"")
//                .replaceFirst("height=\"\\d+\"", "height=\"auto\"");
//
//
//        pdf.setDefaultPageSize(new PageSize(PageSize.A4));
//
//        InputStream adjustedPassageSvgInputStream = new ByteArrayInputStream(adjustedPassageSvgContent.getBytes(StandardCharsets.UTF_8));
//        ISvgConverterProperties passageProperties = new SvgConverterProperties().setBaseUri("");
//        SvgConverter.drawOnDocument(adjustedPassageSvgInputStream, pdf, pageNumber, passageProperties);
//
//        InputStream adjustedQuestionSvgInputStream = new ByteArrayInputStream(adjustedQuestionSvgContent.getBytes(StandardCharsets.UTF_8));
//        ISvgConverterProperties questionProperties = new SvgConverterProperties().setBaseUri("");
//        SvgConverter.drawOnDocument(adjustedQuestionSvgInputStream, pdf, pageNumber, questionProperties);
//
//
//    }



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

        if(response.statusCode()==200){
            String responseBody = response.body();
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

            if(responseJson != null && "Y".equals(responseJson.get("successYn").getAsString())){
                List<ExamSaveListDTO.ItemInfo> itemInfoList = new ArrayList<>();

                if(responseJson.has("itemList")){
                    for(int i = 0; i < responseJson.get("itemList").getAsJsonArray().size(); i++){
                        JsonObject itemObject = responseJson.get("itemList").getAsJsonArray().get(i).getAsJsonObject();

                        ExamSaveListDTO.ItemInfo itemInfo = new ExamSaveListDTO.ItemInfo();
                        itemInfo.setItemNo(itemObject.get("itemNo").getAsInt());
                        itemInfo.setItemId(itemObject.get("itemId").getAsInt());
                        itemInfo.setQuestionFormCode(itemObject.get("questionFormCode").getAsString());
                        itemInfo.setQuestionFormName(itemObject.get("questionFormName").getAsString());
//                        itemInfo.setDifficultyCode(itemObject.get("difficultyCode").getAsString());
                        itemInfo.setDifficultyName(itemObject.get("difficultyName").getAsString());
//                        itemInfo.setLargeChapterId(itemObject.get("largeChapterId").getAsInt());
                        itemInfo.setLargeChapterName(itemObject.get("largeChapterName").getAsString());
//                        itemInfo.setMediumChapterId(itemObject.get("mediumChapterId").getAsInt());
                        itemInfo.setMediumChapterName(itemObject.get("mediumChapterName").getAsString());
//                        itemInfo.setSmallChapterId(itemObject.get("smallChapterId").getAsInt());
//                        itemInfo.setSmallChapterName(itemObject.get("smallChapterName").getAsString());
//                        itemInfo.setTopicChapterId(itemObject.get("topicChapterId").getAsInt());
//                        itemInfo.setTopicChapterName(itemObject.get("topicChapterName").getAsString());
                        itemInfo.setPassageId(itemObject.get("passageId").getAsInt());
                        itemInfo.setPassageUrl(itemObject.get("passageUrl").getAsString());
                        itemInfo.setQuestionUrl(itemObject.get("questionUrl").getAsString());
//                        itemInfo.setAnswerUrl(itemObject.get("answerUrl").getAsString());
//                        itemInfo.setExplainUrl(itemObject.get("explainUrl").getAsString());

                        itemInfoList.add(itemInfo);
                    }
                }
                return itemInfoList;
            }
        }

        return Collections.emptyList();
    }

}