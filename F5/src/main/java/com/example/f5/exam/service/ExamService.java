package com.example.f5.exam.service;

import com.example.f5.exam.dto.ExamDto;
import com.example.f5.temporary.entity.TemporaryQuestion;
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

        for (JsonElement itemElement : itemArray) {
            ExamDto.itemInfoResponse responseDto = new ExamDto.itemInfoResponse();

            JsonObject itemObject = itemElement.getAsJsonObject();
            responseDto.setItemId(itemObject.get("itemId").getAsInt());
            responseDto.setQuestionFormName(itemObject.get("questionFormName").getAsString());
            responseDto.setDifficultyName(itemObject.get("difficultyName").getAsString());
            responseDto.setChapterName(String.join(" > ", itemObject.get("largeChapterName").getAsString(), itemObject.get("mediumChapterName").getAsString(), itemObject.get("smallChapterName").getAsString(), itemObject.get("topicChapterName").getAsString()));
            responseDto.setQuestionUrl(itemObject.get("questionUrl").getAsString());
            responseDto.setAnswerUrl(itemObject.get("answerUrl").getAsString());
            responseDto.setExplainUrl(itemObject.get("explainUrl").getAsString());

            itemList.add(responseDto);
        }
        return itemList;
    }

    public List<ExamDto.CategoryResponse> getCategory(int itemId) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("subjectId", itemId);

        HttpResponse<String> response = apiPostRequest("https://tsherpa.item-factory.com/chapter/chapter-list", jsonMap);
        String responseBody = response.body();
        JsonArray itemArray = JsonParser.parseString(responseBody).getAsJsonObject().getAsJsonArray("chapterList");

        List<ExamDto.CategoryResponse> itemList = new ArrayList<>();

        for (JsonElement itemElement : itemArray) {
            JsonObject itemObject = itemElement.getAsJsonObject();
            ExamDto.CategoryResponse responseDto = new ExamDto.CategoryResponse();

            responseDto.setCurriculumCode(itemObject.get("curriculumCode").getAsString());
            responseDto.setCurriculumName(itemObject.get("curriculumName").getAsString());
            responseDto.setSubjectId(itemObject.get("subjectId").getAsInt());
            responseDto.setSubjectName(itemObject.get("subjectName").getAsString());
            responseDto.setLargeChapterId(itemObject.get("largeChapterId").getAsInt());
            responseDto.setLargeChapterName(itemObject.get("largeChapterName").getAsString());
            responseDto.setMediumChapterId(itemObject.get("mediumChapterId").getAsInt());
            responseDto.setMediumChapterName(itemObject.get("mediumChapterName").getAsString());
            responseDto.setSmallChapterId(itemObject.get("smallChapterId").getAsInt());
            responseDto.setSmallChapterName(itemObject.get("smallChapterName").getAsString());
            responseDto.setTopicChapterId(itemObject.get("topicChapterId").getAsInt());
            responseDto.setTopicChapterName(itemObject.get("topicChapterName").getAsString());

            itemList.add(responseDto);
        }
        return itemList;
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
}
