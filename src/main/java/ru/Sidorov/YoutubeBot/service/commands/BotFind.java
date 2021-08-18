package ru.Sidorov.YoutubeBot.service.commands;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.Sidorov.YoutubeBot.dto.ResponseByNameChannel;

public class BotFind {
    public static void yBotFind(String channelName, String videoName, Integer roomId, RestTemplate restTemplate, String apiKey) {

        HttpHeaders headersAnswer = new HttpHeaders();
        headersAnswer.set("Accept", "*/*");//setContentType(MediaType.);

        RestTemplate youtubeGetIdByNameChannel = new RestTemplate();
        ResponseEntity<ResponseByNameChannel> responseByNameChannel = youtubeGetIdByNameChannel.getForEntity(
                "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=" +
                        channelName +
                        "&key=" +
                        apiKey +
                        "&maxResults=1&type=channel", ResponseByNameChannel.class);

        String myMessage = "Такой канал не найден";
        String channelId = responseByNameChannel.getBody().getItems().size() > 0 ? responseByNameChannel.getBody().getItems().get(0).getId().getChannelId() : "";
        String videoId ="";
        if (!channelId.isEmpty()) {
            RestTemplate youtubeGetVideoByIdChannelandVideoName = new RestTemplate();
            ResponseEntity<ResponseByNameChannel> responseByIdVideo = youtubeGetVideoByIdChannelandVideoName.getForEntity(
                    "https://youtube.googleapis.com/youtube/v3/search?part=snippet&channelId=" +
                            channelId +
                            "&q=" +
                            videoName +
                            "&key=" +
                            apiKey +
                            "&maxResults=1&type=video", ResponseByNameChannel.class);

            myMessage = "Имя канала: " + responseByNameChannel.getBody().getItems().get(0).getSnippet().getTitle();//responseByNameChannel.getBody().getItems().get(0).getId().getChannelId();
            myMessage += "<br/><a href='https://www.youtube.com/watch?v=" +
                    responseByIdVideo.getBody().getItems().get(0).getId().getVideoId()+ "'>" +
                    responseByIdVideo.getBody().getItems().get(0).getSnippet().getTitle() + "</a>";
        } else
        {
            RestTemplate youtubeGetVideoByIdChannelandVideoName = new RestTemplate();
            ResponseEntity<ResponseByNameChannel> responseByIdVideo = youtubeGetVideoByIdChannelandVideoName.getForEntity(
                    "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=" +
                            videoName +
                            "&key=" +
                            apiKey +
                            "&maxResults=1&type=video", ResponseByNameChannel.class);

            videoId = responseByIdVideo.getBody().getItems().get(0).getId().getVideoId();
            myMessage = "Название видео: ";
            myMessage += "<br/><a href='https://www.youtube.com/watch?v=" +
                    responseByIdVideo.getBody().getItems().get(0).getId().getVideoId()+ "'>" +
                    responseByIdVideo.getBody().getItems().get(0).getSnippet().getTitle() + "</a>";
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("roomId", String.valueOf(roomId));
        map.add("message", myMessage);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersAnswer);
        restTemplate.postForEntity("http://localhost:8080/messageSend", request, String.class);
    }
}
