package com.example.f5.exam.service;

import com.example.f5.exam.dto.ExamDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExamService {

    public List<ExamDto.itemInfoResponse> getItemList(ExamDto.itemInfoRequest requestDto) {
        HttpResponse<String> response = apiPostRequest("https://tsherpa.item-factory.com/item-img/chapters/item-list", requestDto);

        String responseBody = response.body();
        JsonArray itemArray = JsonParser.parseString(responseBody).getAsJsonObject().getAsJsonArray("itemList");

        List<ExamDto.itemInfoResponse> itemList = new ArrayList<>();

        int totalCount = 0;

        for (int i = 0; i < requestDto.getLevelCnt().size(); i++) {
            totalCount += requestDto.getLevelCnt().get(i);
        }

        if (totalCount != itemArray.size()){
            for (int i = 0; i < totalCount; i++) {
                JsonElement itemElement = itemArray.get(i);
                ExamDto.itemInfoResponse responseDto = new ExamDto.itemInfoResponse();

                JsonObject itemObject = itemElement.getAsJsonObject();
                responseDto.setItemId(itemObject.get("itemId").getAsInt());
                responseDto.setDifficultyName(itemObject.get("difficultyName").getAsString());
                responseDto.setChapterName(String.join(" > ", itemObject.get("largeChapterName").getAsString(), itemObject.get("mediumChapterName").getAsString(), itemObject.get("smallChapterName").getAsString(), itemObject.get("topicChapterName").getAsString()));
                responseDto.setQuestionUrl(itemObject.get("questionUrl").getAsString());
                responseDto.setAnswerUrl(itemObject.get("answerUrl").getAsString());
                responseDto.setExplainUrl(itemObject.get("explainUrl").getAsString());

                if (itemObject.get("passageUrl") != null && !itemObject.get("passageUrl").isJsonNull()){
                    responseDto.setPassageUrl(itemObject.get("passageUrl").getAsString());
                }

                if (Integer.parseInt(itemObject.get("questionFormCode").getAsString()) <= 50){
                    responseDto.setQuestionFormName("객관식");
                } else {
                    responseDto.setQuestionFormName("주관식");
                }

                itemList.add(responseDto);

            }
        } else {

            for (JsonElement itemElement : itemArray) {
                ExamDto.itemInfoResponse responseDto = new ExamDto.itemInfoResponse();

                JsonObject itemObject = itemElement.getAsJsonObject();
                responseDto.setItemId(itemObject.get("itemId").getAsInt());
                responseDto.setDifficultyName(itemObject.get("difficultyName").getAsString());
                responseDto.setChapterName(String.join(" > ", itemObject.get("largeChapterName").getAsString(), itemObject.get("mediumChapterName").getAsString(), itemObject.get("smallChapterName").getAsString(), itemObject.get("topicChapterName").getAsString()));
                responseDto.setQuestionUrl(itemObject.get("questionUrl").getAsString());
                responseDto.setAnswerUrl(itemObject.get("answerUrl").getAsString());
                responseDto.setExplainUrl(itemObject.get("explainUrl").getAsString());

                // questionFormCode 50이하면 객관식으로 변형해서 담고, 50초과면 주관식으로 담기
                // questionFormCode를 인트로 파싱해서 해야됨
                // 여기에  questionFormCode를 questionFormName으로 변경해서 딤기

                if (itemObject.get("passageUrl") != null && !itemObject.get("passageUrl").isJsonNull()) {
                    responseDto.setPassageUrl(itemObject.get("passageUrl").getAsString());
                }

                if (Integer.parseInt(itemObject.get("questionFormCode").getAsString()) <= 50) {
                    responseDto.setQuestionFormName("객관식");
                } else {
                    responseDto.setQuestionFormName("주관식");
                }

                itemList.add(responseDto);
            }
        }
        return itemList;
    }

    public ExamDto.Curriculum getCategory(int itemId) throws JsonProcessingException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("subjectId", itemId);

        HttpResponse<String> response = apiPostRequest("https://tsherpa.item-factory.com/chapter/chapter-list", jsonMap);
        String responseBody = response.body();
        JsonArray itemArray = JsonParser.parseString(responseBody).getAsJsonObject().getAsJsonArray("chapterList");

        return transformJson(String.valueOf(itemArray));
    }

    @SneakyThrows
    private HttpResponse<String> apiPostRequest(String apiUrl, Object body) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(body);

        // HttpClient 객체 생성
        HttpClient httpClient = HttpClient.newHttpClient();

        // POST 요청 준비
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        // 요청 보내기
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }


    public ExamDto.Curriculum transformJson(String inputJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // Input JSON을 POJO로 변환
        ExamDto.CategoryResponse[] inputCurriculums = mapper.readValue(inputJson, ExamDto.CategoryResponse[].class);
        return transformToStructuredData(inputCurriculums);
    }

    public ExamDto.Curriculum transformToStructuredData(ExamDto.CategoryResponse[] inputCurriculums) {
        ExamDto.Curriculum resultCurriculum = new ExamDto.Curriculum();
        resultCurriculum.setCurriculumCode(inputCurriculums[0].getCurriculumCode());
        resultCurriculum.setCurriculumName(inputCurriculums[0].getCurriculumName());
        resultCurriculum.setSubjectId(inputCurriculums[0].getSubjectId());
        resultCurriculum.setSubjectName(inputCurriculums[0].getSubjectName());


        Set<ExamDto.Curriculum.LargeChapter> uniqueLargeChapters = new HashSet<>();
        Set<ExamDto.Curriculum.LargeChapter.MediumChapter> uniqueMediumChapters = new HashSet<>();
        Set<ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter> uniqueSmallChapters = new HashSet<>();
        Set<ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter.TopicChapter> uniqueTopicChapters = new HashSet<>();

        for (ExamDto.CategoryResponse inputCurriculum : inputCurriculums) {

            // LargeChapter
            ExamDto.Curriculum.LargeChapter currentLargeChapter = new ExamDto.Curriculum.LargeChapter();
            currentLargeChapter.setLargeChapterId(inputCurriculum.getLargeChapterId());
            currentLargeChapter.setLargeChapterName(inputCurriculum.getLargeChapterName());

            if (uniqueLargeChapters.add(currentLargeChapter)) {
                resultCurriculum.getChapters().add(currentLargeChapter);
            }

            // MediumChapter
            ExamDto.Curriculum.LargeChapter.MediumChapter currentMediumChapter = new ExamDto.Curriculum.LargeChapter.MediumChapter();
            currentMediumChapter.setMediumChapterId(inputCurriculum.getMediumChapterId());
            currentMediumChapter.setMediumChapterName(inputCurriculum.getMediumChapterName());

            if (uniqueMediumChapters.add(currentMediumChapter)) {
                findLargeChapterById(resultCurriculum, inputCurriculum.getLargeChapterId())
                        .ifPresent(lc -> lc.getMediumChapters().add(currentMediumChapter));
            }

            // SmallChapter
            ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter currentSmallChapter = new ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter();
            currentSmallChapter.setSmallChapterId(inputCurriculum.getSmallChapterId());
            currentSmallChapter.setSmallChapterName(inputCurriculum.getSmallChapterName());

            if (uniqueSmallChapters.add(currentSmallChapter)) {
                findMediumChapterById(resultCurriculum, inputCurriculum.getMediumChapterId())
                        .ifPresent(mc -> mc.getSmallChapters().add(currentSmallChapter));
            }

            // TopicChapter
            ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter.TopicChapter currentTopicChapter = new ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter.TopicChapter();
            currentTopicChapter.setTopicChapterId(inputCurriculum.getTopicChapterId());
            currentTopicChapter.setTopicChapterName(inputCurriculum.getTopicChapterName());

            if (uniqueTopicChapters.add(currentTopicChapter)) {
                findSmallChapterById(resultCurriculum, inputCurriculum.getSmallChapterId())
                        .ifPresent(sc -> sc.getTopicChapters().add(currentTopicChapter));
            }
        }
        return resultCurriculum;
    }

    // Helper methods to find the right chapter by ID
    private Optional<ExamDto.Curriculum.LargeChapter> findLargeChapterById(ExamDto.Curriculum curriculum, Long id) {
        return curriculum.getChapters().stream()
                .filter(lc -> Objects.equals(lc.getLargeChapterId(), id))
                .findFirst();
    }

    private Optional<ExamDto.Curriculum.LargeChapter.MediumChapter> findMediumChapterById(ExamDto.Curriculum curriculum, Long id) {
        return curriculum.getChapters().stream()
                .flatMap(lc -> lc.getMediumChapters().stream())
                .filter(mc -> Objects.equals(mc.getMediumChapterId(), id))
                .findFirst();
    }

    private Optional<ExamDto.Curriculum.LargeChapter.MediumChapter.SmallChapter> findSmallChapterById(ExamDto.Curriculum curriculum, Long id) {
        return curriculum.getChapters().stream()
                .flatMap(lc -> lc.getMediumChapters().stream())
                .flatMap(mc -> mc.getSmallChapters().stream())
                .filter(sc -> Objects.equals(sc.getSmallChapterId(), id))
                .findFirst();
    }

    public List<String> getEvaluation(int itemId) throws JsonProcessingException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("subjectId", itemId);

        HttpResponse<String> response = apiPostRequest("https://tsherpa.item-factory.com/chapter/evaluation-list", jsonMap);
        String responseBody = response.body();
        JsonArray itemArray = JsonParser.parseString(responseBody).getAsJsonObject().getAsJsonArray("evaluationList");

        List<String> result = new ArrayList<>();
        for (JsonElement jsonElement : itemArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            result.add(jsonObject.get("domainId").getAsString());
            result.add(jsonObject.get("domainName").getAsString());
        }

        return result;
    }
}