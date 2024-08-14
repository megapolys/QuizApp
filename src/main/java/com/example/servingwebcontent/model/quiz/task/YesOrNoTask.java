package com.example.servingwebcontent.model.quiz.task;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class YesOrNoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 1000)
    private String preQuestionText;
    @Column(length = 1000)
    private String questionText;
    private String fileName;

    private Float yesWeight;
    private Float noWeight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreQuestionText() {
        return preQuestionText;
    }

    public void setPreQuestionText(String preQuestionText) {
        this.preQuestionText = preQuestionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Float getYesWeight() {
        return yesWeight;
    }

    public void setYesWeight(Float yesWeight) {
        this.yesWeight = yesWeight;
    }

    public Float getNoWeight() {
        return noWeight;
    }

    public void setNoWeight(Float noWeight) {
        this.noWeight = noWeight;
    }
}
