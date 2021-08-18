package ru.Sidorov.YoutubeBot;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.client.RestTemplate;
import ru.Sidorov.YoutubeBot.dto.MessageSend;
import ru.Sidorov.YoutubeBot.dto.SecretKeyDto;
import ru.Sidorov.YoutubeBot.service.commands.BotFind;
import ru.Sidorov.YoutubeBot.service.commands.ChangelInfo;
import ru.Sidorov.YoutubeBot.service.commands.Help;
import ru.Sidorov.YoutubeBot.service.commands.VideoCommentRandom;

import java.lang.reflect.Type;

public class StompSessionHandlerGetMessage implements StompSessionHandler {
    SecretKeyDto secretKeyDto;
    RestTemplate restTemplate;
    String apiKey;

    public StompSessionHandlerGetMessage(SecretKeyDto secretKeyDto, RestTemplate restTemplate, String apiKey){
        this.secretKeyDto = secretKeyDto;
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("connected");
        session.subscribe("/topic/" + secretKeyDto.getKey() + "/sendMessage", this);

    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("error " + exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        System.out.println("error transport " + throwable.getMessage());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return MessageSend.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        MessageSend messageSend = (MessageSend) payload;
        String[] words = messageSend.getText().split("\\{|}\\{|}");
        String command = words[0];
        for (String word : words) {
            System.out.println(word);
        }
        if (command.equals("yBot help"))
            Help.yHelp(messageSend.getRoomId(), restTemplate);
        if (words.length>=2 && command.equals("yBot find "))
            BotFind.yBotFind(words[1], words.length > 2 ? words[2] : words[1], messageSend.getRoomId(), restTemplate, apiKey);
        if (words.length>=2 && command.equals("yBot channelInfo "))
            ChangelInfo.changelInfo(words[1], messageSend.getRoomId(), restTemplate, apiKey);
        if (words.length>=2 && command.equals("yBot videoCommentRandom "))
            VideoCommentRandom.videoCommentRandom(words[1], words.length > 2 ? words[2] : words[1], messageSend.getRoomId(), restTemplate, apiKey);
    }
}
