package ru.Sidorov.YoutubeBot;


import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.Sidorov.YoutubeBot.dto.SecretKeyDto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

public class YoutubeBotApplication {

    public static String apiKey;

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("bot.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        apiKey = properties.getProperty("apiKey");

        //System.out.println(properties.getProperty("username") + ":" + properties.getProperty("password"));
        //get secret key
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(properties.getProperty("username"), properties.getProperty("password")));
        //restTemplate.getInterceptors().add(new LoggingRequestInterceptor());
        String fooResourceUrl = "http://localhost:8080/getUserKey";
        ResponseEntity<SecretKeyDto> response = restTemplate.getForEntity(fooResourceUrl, SecretKeyDto.class);
        //System.out.println(response.getBody().getKey());

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        StompHeaders connectHeaders = new StompHeaders();
        String usernameAndPassword = properties.getProperty("username") + ":" + properties.getProperty("password");
        String basicEncoded = Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());
        connectHeaders.add("Authorization", "Basic "+ basicEncoded);
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        webSocketHttpHeaders.add("Authorization", "Basic "+ basicEncoded);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.connect("ws://localhost:8080/chat", webSocketHttpHeaders, connectHeaders, new StompSessionHandlerGetMessage(response.getBody(), restTemplate, apiKey));
        new Scanner(System.in).nextLine(); // Don't close immediately.
    }

}
