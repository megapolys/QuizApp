package com.example.servingwebcontent.domain.quiz.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class YesOrNoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
