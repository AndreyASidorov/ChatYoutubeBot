package ru.Sidorov.YoutubeBot.service.commands;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.Sidorov.YoutubeBot.dto.ResponseByNameChannel;
import ru.Sidorov.YoutubeBot.dto.ResponseByVideoId;

import java.util.ArrayList;

public class VideoCommentRandom {
    public static void videoCommentRandom(String channelName, String videoName, Integer roomId, RestTemplate restTemplate, String apiKey) {

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

            myMessage = "Название видео: ";
            myMessage += "<br/><a href='https://www.youtube.com/watch?v=" +
                    responseByIdVideo.getBody().getItems().get(0).getId().getVideoId()+ "'>" +
                    responseByIdVideo.getBody().getItems().get(0).getSnippet().getTitle() + "</a>";
            videoId = responseByIdVideo.getBody().getItems().get(0).getId().getVideoId();
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
        if (!videoId.isEmpty()) {
            RestTemplate youtubeGetVideoByVideoId = new RestTemplate();
            ResponseEntity<ResponseByVideoId> responseByVideoId = youtubeGetVideoByVideoId.getForEntity(
                    "https://www.googleapis.com/youtube/v3/commentThreads?part=snippet&videoId=" +
                            videoId +
                            "&key=" +
                            apiKey +
                            "&textFormat=plainText", ResponseByVideoId.class);
            ArrayList<String[]> authorsAndComments = new ArrayList<String[]>();
            String randomAuthor;
            String randomComment;
            if (responseByVideoId.getBody().getItems().size() > 0) {
                for (int i = 0; i < responseByVideoId.getBody().getItems().size(); i++) {
                    String[] authorAndComment = {responseByVideoId.getBody().getItems().get(i).getSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName(),
                            responseByVideoId.getBody().getItems().get(i).getSnippet().getTopLevelComment().getSnippet().getTextDisplay()};
                    authorsAndComments.add(authorAndComment);
                }
                Integer randomIndex = (int) Math.random() * authorsAndComments.size();
                randomAuthor = authorsAndComments.get(randomIndex)[0];
                randomComment = authorsAndComments.get(randomIndex)[1];
            } else {
                randomAuthor = "";
                randomComment = "Комментарии не найдены";
            }
            myMessage +=  "<br/>" + randomAuthor + "<br/>" + randomComment;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("roomId", String.valueOf(roomId));
        map.add("message", myMessage);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersAnswer);
        restTemplate.postForEntity("http://localhost:8080/messageSend", request, String.class);
    }
}
