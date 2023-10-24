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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamService {

    @SneakyThrows
    public List<ExamDto.itemInfoResponse> getItemList(ExamDto.itemInfoRequest requestDto) {
        String apiUrl = "https://tsherpa.item-factory.com/item-img/chapters/item-list";

        Gson gson = new Gson();

        String jsonString = gson.toJson(requestDto);


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
}
