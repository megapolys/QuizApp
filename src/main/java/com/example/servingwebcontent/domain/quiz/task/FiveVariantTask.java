package com.example.servingwebcontent.domain.quiz.task;

import com.example.servingwebcontent.domain.quiz.Quiz;
import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "quiz_task_five_variant")
public class FiveVariantTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 1000)
    private String preQuestionText;
    @Column(length = 1000)
    private String questionText;
    private String fileName;

    private Float firstWeight;
    private Float secondWeight;
    private Float thirdWeight;
    private Float fourthWeight;
    private Float fifthWeight;

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

    public Float getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Float firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Float getSecondWeight() {
        return secondWeight;
    }

    public void setSecondWeight(Float secondWeight) {
        this.secondWeight = secondWeight;
    }

    public Float getThirdWeight() {
        return thirdWeight;
    }

    public void setThirdWeight(Float thirdWeight) {
        this.thirdWeight = thirdWeight;
    }

    public Float getFourthWeight() {
        return fourthWeight;
    }

    public void setFourthWeight(Float fourthWeight) {
        this.fourthWeight = fourthWeight;
    }

    public Float getFifthWeight() {
        return fifthWeight;
    }

    public void setFifthWeight(Float fifthWeight) {
        this.fifthWeight = fifthWeight;
    }

}
