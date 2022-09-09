package com.example.mytrivia.model;

import java.util.ArrayList;


public class Question {

    private String category;
    private String id;
    private String correctAnswer;
    private ArrayList<String> incorrectAnswers;
    private String question;
    private ArrayList<String> tags;
    private String type;
    private String difficulty;
    private ArrayList<Object> regions;
    private ArrayList<String> allAnswers = new ArrayList<>();

    public ArrayList<String> getAllAnswers() {
        return allAnswers;
    }

    public void setAllAnswers(ArrayList<String> allAnswers) {
        this.allAnswers = allAnswers;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public ArrayList<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public ArrayList<Object> getRegions() {
        return regions;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setIncorrectAnswers(ArrayList<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setRegions(ArrayList<Object> regions) {
        this.regions = regions;
    }
}
