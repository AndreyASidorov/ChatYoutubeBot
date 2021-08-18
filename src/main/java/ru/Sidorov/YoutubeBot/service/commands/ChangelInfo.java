package ru.Sidorov.YoutubeBot.service.commands;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.Sidorov.YoutubeBot.dto.Items;
import ru.Sidorov.YoutubeBot.dto.ResponseByNameChannel;

public class ChangelInfo {
    public static void changelInfo(String channelName, Integer roomId, RestTemplate restTemplate, String apiKey) {

        HttpHeaders headersAnswer = new HttpHeaders();
        headersAnswer.set("Accept", "*/*");//setContentType(MediaType.);

        ResponseEntity<ResponseByNameChannel> responseByNameChannel = changelInfoResponseByNameChannel(channelName, apiKey);

        String myMessage = "Такое видео не найдено";
        String channelId = responseByNameChannel.getBody().getItems().size() > 0 ? responseByNameChannel.getBody().getItems().get(0).getId().getChannelId() : "";
        if (!channelId.isEmpty()) {
            RestTemplate youtubeGetFiveVideoByIdChannel = new RestTemplate();
            ResponseEntity<ResponseByNameChannel> responseByIdChannel = youtubeGetFiveVideoByIdChannel.getForEntity(
                    "https://youtube.googleapis.com/youtube/v3/search?part=snippet&channelId=" +
                            channelId +
                            "&order=date&key=" +
                            apiKey +
                            "&maxResults=5&type=video", ResponseByNameChannel.class);

            myMessage = "Имя канала: " + responseByNameChannel.getBody().getItems().get(0).getSnippet().getTitle();//responseByNameChannel.getBody().getItems().get(0).getId().getChannelId();
            myMessage += "<br/>Список видео:";
            for (Items items : responseByIdChannel.getBody().getItems()) {
                myMessage += "<br/><a href='https://www.youtube.com/watch?v=" +
                        items.getId().getVideoId() + "'>" +
                        items.getSnippet().getTitle() + "</a>";//"\n <iframe width='560' height='315' src='https://www.youtube.com/watch?v="+items.getId().getVideoId()+"'></iframe>";
            }
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("roomId", String.valueOf(roomId));
        map.add("message", myMessage);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersAnswer);
        restTemplate.postForEntity("http://localhost:8080/messageSend", request, String.class);
    }
    public static ResponseEntity<ResponseByNameChannel> changelInfoResponseByNameChannel(String channelName, String apiKey){

        RestTemplate youtubeGetIdByNameChannel = new RestTemplate();
        ResponseEntity<ResponseByNameChannel> responseByNameChannel = youtubeGetIdByNameChannel.getForEntity(
                "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=" +
                        channelName +
                        "&key=" +
                        apiKey +
                        "&maxResults=1&type=channel", ResponseByNameChannel.class);
        return responseByNameChannel;
    }
}
