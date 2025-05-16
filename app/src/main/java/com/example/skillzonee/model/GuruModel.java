package com.example.skillzonee.model;

import java.util.List;

public class GuruModel {
    private String id;
    private String title;
    private String subtitle;
    private String time;
    private List<QuestionModel> questionList;

    public GuruModel() {}

    public GuruModel(String id, String title, String subtitle, String time, List<QuestionModel> questionList) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.time = time;
        this.questionList = questionList;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public List<QuestionModel> getQuestionList() { return questionList; }
    public void setQuestionList(List<QuestionModel> questionList) { this.questionList = questionList; }

    public static class QuestionModel {
        private String question;
        private String correct;
        private List<String> options;

        public QuestionModel() {}

        public QuestionModel(String question, String correct, List<String> options) {
            this.question = question;
            this.correct = correct;
            this.options = options;
        }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public String getCorrect() { return correct; }
        public void setCorrect(String correct) { this.correct = correct; }

        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }
    }
}