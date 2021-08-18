package ru.Sidorov.YoutubeBot.dto;

public class Items {
    ChannelAndVideoId id;
    Snippet snippet;

    public ChannelAndVideoId getId() {
        return id;
    }

    public void setId(ChannelAndVideoId id) {
        this.id = id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }
}
