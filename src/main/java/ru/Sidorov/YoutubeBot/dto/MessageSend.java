package ru.Sidorov.YoutubeBot.dto;

import java.util.Date;

public class MessageSend {
    private String author;
    private String date;
    private String text;
    private Integer messageId;
    private Integer roomId;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public MessageSend(String author, String date, String text, Integer messageId, Integer roomId) {
        this.author = author;
        this.date = date;
        this.text = text;
        this.messageId = messageId;
        this.roomId = roomId;
    }

    public MessageSend() {
    }
}
