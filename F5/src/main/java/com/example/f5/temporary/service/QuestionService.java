package com.example.f5.temporary.service;

import com.example.f5.temporary.entity.DeletedQuestion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    //        List<Integer> itemIdList = new ArrayList<>();
//
//        for (String item : items) {
//            itemIdList.add(Integer.parseInt(item));
//        }
    @SneakyThrows
    public List<SimilarObject> similarQuestion(List<String> items) {
        String similarUrl = "https://tsherpa.item-factory.com/item-img/similar-list";

        HttpClient httpClient = HttpClient.newHttpClient();

        // JSON 데이터로 변환
        String requestBody = "{\"itemIdList\":" + items + "}";

        // POST 요청 생성
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(similarUrl))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // POST 요청을 보내고 JSON 응답 받기
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // 성공적인 응답을 받은 경우
            String jsonResponse = response.body();

            JsonArray itemArray = JsonParser.parseString(jsonResponse).getAsJsonObject().getAsJsonArray("itemList");

            List<SimilarObject> itemList = new ArrayList<>();

            for (JsonElement itemElement : itemArray) {
                SimilarObject responseDto = new SimilarObject();

                JsonObject itemObject = itemElement.getAsJsonObject();
                responseDto.setItemId(itemObject.get("itemId").getAsInt());
//                responseDto.setQuestionFormName(itemObject.get("questionFormName").getAsString());
                responseDto.setQuestionFormCode(itemObject.get("questionFormCode").getAsString());
                responseDto.setDifficultyName(itemObject.get("difficultyName").getAsString());
                responseDto.setChapterName(String.join(" > ", itemObject.get("largeChapterName").getAsString(), itemObject.get("mediumChapterName").getAsString(), itemObject.get("smallChapterName").getAsString(), itemObject.get("topicChapterName").getAsString()));
                responseDto.setQuestionUrl(itemObject.get("questionUrl").getAsString());
                responseDto.setAnswerUrl(itemObject.get("answerUrl").getAsString());
                responseDto.setExplainUrl(itemObject.get("explainUrl").getAsString());

                itemList.add(responseDto);
            }
            return itemList;

        } else {
            // 에러 응답을 받은 경우
            return null;
        }
    }

    public String deleteQuestion(DeletedQuestion request) {
        return "";
    }


}
