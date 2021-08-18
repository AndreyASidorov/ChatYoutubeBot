package ru.Sidorov.YoutubeBot.service.commands;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class Help {
    public static void yHelp(Integer roomId, RestTemplate restTemplate) {
        HttpHeaders headersAnswer = new HttpHeaders();
        headersAnswer.set("Accept", "*/*");//setContentType(MediaType.);
        String myMessage = "Список команд бота:" +
                "<br/>yBot find {имя канала}{Название ролика}" +
                "<br/>changelInfo {имя канала}" +
                "<br/>videoCommentRanom {имя канала}{Название ролика}";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("roomId", String.valueOf(roomId));
        map.add("message", myMessage);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersAnswer);
        restTemplate.postForEntity("http://localhost:8080/messageSend", request, String.class);
    }
}
