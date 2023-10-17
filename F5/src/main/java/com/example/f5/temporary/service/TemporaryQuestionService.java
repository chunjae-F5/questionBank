package com.example.f5.temporary.service;

import com.example.f5.temporary.dto.TemporaryQuestionDto;
import com.example.f5.temporary.entity.TemporaryQuestion;
import com.example.f5.temporary.repository.TemporaryQuestionRepository;
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
public class TemporaryQuestionService {
    private final TemporaryQuestionRepository tqRepository;

    @SneakyThrows
    public void saveQuestionService() {
        List<Integer> problemCodeList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        String apiUrl = "https://doctor.item-factory.com/items/item/item-list";

        // HttpClient 객체 생성
        HttpClient httpClient = HttpClient.newHttpClient();

        // POST 요청 준비
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"problemCodeArr\":" + problemCodeList + "}"))
                .build();

        // 요청 보내기
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // 응답 출력
//        System.out.println("Response Code: " + response.statusCode());
//        System.out.println("Response Body: " + response.body());

        String responseBody = response.body();
        JsonArray itemArray = JsonParser.parseString(responseBody).getAsJsonObject().getAsJsonArray("items");

        List<TemporaryQuestion> itemList = new ArrayList<>();

        for (JsonElement itemElement : itemArray) {
            JsonObject itemObject = itemElement.getAsJsonObject();
            int number = itemObject.get("questionNo").getAsInt();
            String type = String.join(" > ", itemObject.get("largeChapterName").getAsString(), itemObject.get("mediumChapterName").getAsString(), itemObject.get("smallChapterName").getAsString(), itemObject.get("topicChapterName").getAsString());
            String form = itemObject.get("questionFormCodeName").getAsString();
            String level = itemObject.get("questionLevelName").getAsString();
            String questionHtml = itemObject.get("questionHtml").getAsString();
            String answerHtml = itemObject.get("answerHtml").getAsString();
            String explainHtml = itemObject.get("explainHtml").getAsString();

            TemporaryQuestion item = new TemporaryQuestion(null, number, type, form, level, questionHtml, answerHtml, explainHtml);
            itemList.add(item);
        }

        tqRepository.saveAll(itemList);
    }
}
