package ru.Sidorov.YoutubeBot.dto;

import java.util.List;

public class ResponseByVideoId {
    List<ItemsComments> items;

    public List<ItemsComments> getItems() {
        return items;
    }

    public void setItems(List<ItemsComments> items) {
        this.items = items;
    }
}
