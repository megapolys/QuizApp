package com.example.servingwebcontent.model.quiz.result;

import com.example.servingwebcontent.model.quiz.QuizTask;
import jakarta.persistence.*;

public class QuizTaskResult {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuizTask task;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id", nullable = false)
    private QuizResult quiz;

    private boolean complete;

    private String variant;
    private Float altScore;
    @Transient
    private Float resultScore;
    private String text;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public QuizTask getTask() {
        return task;
    }

    public void setTask(QuizTask task) {
        this.task = task;
    }

    public QuizResult getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizResult quiz) {
        this.quiz = quiz;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getAltScore() {
        return altScore;
    }

    public void setAltScore(Float altScore) {
        this.altScore = altScore;
    }

    public Float getResultScore() {
        return resultScore;
    }

    public void setResultScore(Float resultScore) {
        this.resultScore = resultScore;
    }
}
