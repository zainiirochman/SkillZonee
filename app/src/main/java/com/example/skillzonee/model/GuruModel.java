package com.example.skillzonee.model;

import java.util.List;

public class GuruModel {
    private String question;
    private List<String> options;
    private String correct;

    public GuruModel() {}

    public GuruModel(String question, List<String> options, String correct) {
        this.question = question;
        this.options = options;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrect() {
        return correct;
    }
}