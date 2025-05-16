package com.example.skillzonee.model;

public class MateriModel {
    private String title;
    private String description;
    private String youtubeLink;

    public MateriModel() {}

    public MateriModel(String title, String description, String youtubeLink) {
        this.title = title;
        this.description = description;
        this.youtubeLink = youtubeLink;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }
}
